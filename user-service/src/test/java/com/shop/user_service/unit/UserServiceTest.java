package com.shop.user_service.unit;

import com.shop.user_service.domain.Role;
import com.shop.user_service.domain.User;
import com.shop.user_service.dto.response.UserResponse;
import com.shop.user_service.exception.NotFoundException;
import com.shop.user_service.mapper.UserMapper;
import com.shop.user_service.repository.UserRepository;
import com.shop.user_service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setRole(Role.USER);

        userResponse = new UserResponse(
                1L,
                "John",
                "Doe",
                "testuser",
                "test@example.com",
                "USER"
        );
    }

    @Test
    void getAllUsers_ReturnsList() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        List<UserResponse> result = userService.getAllUsers(null);

        assertEquals(1, result.size());
        assertEquals("testuser", result.getFirst().username());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_UserExists_ReturnsUserResponse() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("testuser", result.username());
        assertEquals("John", result.firstname());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_UserDoesNotExist_ThrowsNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserById(1L));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void updateUser_Success() {
        userService.updateUser(user);
        verify(userRepository, times(1)).saveAndFlush(user);
    }

    @Test
    void updateRole_UserExists_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.updateRole(1L, "ADMIN");

        assertEquals(Role.ADMIN, user.getRole());
        verify(userRepository, times(1)).saveAndFlush(user);
    }

    @Test
    void updateRole_UserDoesNotExist_ThrowsNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.updateRole(1L, "ADMIN"));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void deleteUser_UserExists_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_UserDoesNotExist_ThrowsNotFoundException() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userService.deleteUser(1L));
        verify(userRepository, times(1)).existsById(1L);
    }
}
