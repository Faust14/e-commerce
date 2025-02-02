package com.shop.user_service.service;

import com.shop.user_service.domain.Role;
import com.shop.user_service.domain.User;
import com.shop.user_service.dto.response.UserResponse;
import com.shop.user_service.exception.NotFoundException;
import com.shop.user_service.mapper.UserMapper;
import com.shop.user_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        return userMapper.toResponse(user);
    }

    @Transactional
    public void updateRole(Long userId, String role) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        user.setRole(Role.getRoleByAlias(role));
        userRepository.saveAndFlush(user);
    }
}
