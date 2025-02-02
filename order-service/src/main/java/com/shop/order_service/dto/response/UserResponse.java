package com.shop.order_service.dto.response;

public record UserResponse(
        Long id,
        String firstname,
        String lastname,
        String username,
        String email,
        String role
        ) {
}
