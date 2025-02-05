package com.shop.user_service.service;

import com.shop.user_service.domain.Role;
import com.shop.user_service.dto.request.CreateUserRequest;
import com.shop.user_service.dto.request.LoginRequest;
import com.shop.user_service.exception.LoginException;
import com.shop.user_service.dto.response.LoginResponse;
import com.shop.user_service.domain.User;
import com.shop.user_service.mapper.UserMapper;
import com.shop.user_service.repository.UserRepository;
import com.shop.user_service.security.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse doLogin(LoginRequest loginRequest) {
        Optional<User> optionalUser = userRepository.findByUsername(loginRequest.username());

        if (optionalUser.isEmpty()) {
            throw new LoginException("Invalid credentials");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new LoginException("Invalid credentials");
        }

        String jwtToken = jwtUtil.generateToken(user);

        return new LoginResponse(jwtToken, user);
    }

    @Transactional
    public void doRegister(CreateUserRequest userRequest) {
        User user = userMapper.toUser(userRequest);
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.saveAndFlush(user);
    }

}
