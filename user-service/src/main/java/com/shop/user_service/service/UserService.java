package com.shop.user_service.service;

import com.shop.user_service.domain.Role;
import com.shop.user_service.domain.User;
import com.shop.user_service.exception.NotFoundException;
import com.shop.user_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;


    @Transactional
    public void updateRole(Long userId, String role) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        user.setRole(Role.getRoleByAlias(role));
        userRepository.saveAndFlush(user);
    }
}
