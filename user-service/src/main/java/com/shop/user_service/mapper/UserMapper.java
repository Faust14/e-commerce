package com.shop.user_service.mapper;

import com.shop.user_service.domain.User;
import com.shop.user_service.dto.request.CreateUserRequest;
import com.shop.user_service.dto.response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toUser(CreateUserRequest createUserRequest) {
        return User.builder()
                .email(createUserRequest.email())
                .firstname(createUserRequest.firstname())
                .lastname(createUserRequest.lastname())
                .username(createUserRequest.username())
                .password(createUserRequest.password())
                .build();
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().getName()
        );
    }
}
