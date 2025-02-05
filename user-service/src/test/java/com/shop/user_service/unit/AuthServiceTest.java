package com.shop.user_service.unit;

import com.shop.user_service.domain.Role;
import com.shop.user_service.dto.request.CreateUserRequest;
import com.shop.user_service.dto.request.LoginRequest;
import com.shop.user_service.exception.LoginException;
import com.shop.user_service.dto.response.LoginResponse;
import com.shop.user_service.domain.User;
import com.shop.user_service.mapper.UserMapper;
import com.shop.user_service.repository.UserRepository;
import com.shop.user_service.security.JwtUtil;
import com.shop.user_service.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private User user;
    private LoginRequest loginRequest;
    private CreateUserRequest createUserRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("hashed_password"); // Mockovana lozinka
        user.setRole(Role.USER);

        loginRequest = new LoginRequest("testuser", "password");

        createUserRequest = new CreateUserRequest(
                "John",
                "Doe",
                "testuser",
                "test@example.com",
                "password"
        );
    }

    @Test
    void doLogin_Success() {
        when(userRepository.findByUsername(loginRequest.username())).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(user)).thenReturn("mockJwtToken");
        when(passwordEncoder.matches(eq(loginRequest.password()), eq(user.getPassword()))).thenReturn(true);

        LoginResponse loginResponse = authService.doLogin(loginRequest);

        assertNotNull(loginResponse);
        assertEquals("mockJwtToken", loginResponse.getJtwToken());
        assertEquals(user, loginResponse.getUser());

        verify(userRepository, times(1)).findByUsername(loginRequest.username());
        verify(jwtUtil, times(1)).generateToken(user);
    }

    @Test
    void doLogin_InvalidUsername_ThrowsLoginException() {
        when(userRepository.findByUsername(loginRequest.username())).thenReturn(Optional.empty());

        assertThrows(LoginException.class, () -> authService.doLogin(loginRequest));
        verify(userRepository, times(1)).findByUsername(loginRequest.username());
    }

    @Test
    void doLogin_InvalidPassword_ThrowsLoginException() {
        when(userRepository.findByUsername(loginRequest.username())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(eq(loginRequest.password()), eq(user.getPassword()))).thenReturn(false);

        Exception exception = assertThrows(LoginException.class, () -> authService.doLogin(loginRequest));
        assertEquals("Invalid credentials", exception.getMessage());

        verify(userRepository, times(1)).findByUsername(loginRequest.username());
    }

    @Test
    void doRegister_Success() {
        User newUser = new User();
        newUser.setId(2L);
        newUser.setUsername("testuser2");
        newUser.setPassword("hashed_password");
        newUser.setRole(Role.USER);

        when(userMapper.toUser(createUserRequest)).thenReturn(newUser);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");

        authService.doRegister(createUserRequest);

        verify(userMapper, times(1)).toUser(createUserRequest);
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userRepository, times(1)).saveAndFlush(newUser);
    }
}