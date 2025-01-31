package com.shop.user_service.service;

import com.shop.user_service.domain.Role;
import com.shop.user_service.dto.request.CreateUserRequest;
import com.shop.user_service.dto.request.LoginRequest;
import com.shop.user_service.exception.LoginException;
import com.shop.user_service.dto.response.LoginResponse;
import com.shop.user_service.domain.User;
import com.shop.user_service.exception.NotFoundException;
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
        Optional<User> optionalUser = userRepository.findByUsernameAndPassword(loginRequest.username(), loginRequest.password());

        if(optionalUser.isEmpty()) {
            throw new LoginException("Invalid credentials");
        }

        String jwtToken = jwtUtil.generateToken(optionalUser.get());
        return new LoginResponse(jwtToken);
    }

    @Transactional
    public void doRegister(CreateUserRequest userRequest) {
        User user = userMapper.toUser(userRequest);
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.saveAndFlush(user);
    }

    @Transactional
    public void updateRole(Long userId, String role) {
        User user = userRepository.findById(userId).orElseThrow(()->new NotFoundException("User not found"));
        user.setRole(Role.getRoleByAlias(role));
        userRepository.saveAndFlush(user);
    }
}
