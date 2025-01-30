package com.shop.user_service.dto.request;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull
        String username,
        @NotNull
        String password
) {
}
