package com.shop.user_service.mapper;

import com.shop.user_service.domain.User;
import com.shop.user_service.dto.request.CreateUserRequest;
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
}
