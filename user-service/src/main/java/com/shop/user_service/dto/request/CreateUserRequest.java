package com.shop.user_service.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(
        @NotNull
        String firstname,
        @NotNull
        String lastname,
        @NotNull
        String username,
        @NotNull
        String email,
        @NotNull
        String password
) {
}
