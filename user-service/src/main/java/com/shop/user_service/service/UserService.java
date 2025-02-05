package com.shop.user_service.service;

import com.shop.user_service.domain.Role;
import com.shop.user_service.domain.User;
import com.shop.user_service.dto.response.UserResponse;
import com.shop.user_service.exception.NotFoundException;
import com.shop.user_service.mapper.UserMapper;
import com.shop.user_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponse> getAllUsers(String search) {
        List<User> users;
        if (search == null || search.trim().isEmpty()) {
            users = userRepository.findAll();
        } else {
            users = userRepository.findBySearchCriteria(search.toLowerCase());
        }
        return users.stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        return userMapper.toResponse(user);
    }

    @Transactional
    public void updateUser(User user) {
        userRepository.saveAndFlush(user);
    }

    @Transactional
    public void updateRole(Long userId, String role) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        user.setRole(Role.getRoleByAlias(role));
        userRepository.saveAndFlush(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found");
        }
        userRepository.deleteById(userId);
    }
}
