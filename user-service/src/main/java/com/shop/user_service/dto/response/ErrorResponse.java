package com.shop.user_service.dto.response;

public record ErrorResponse (
        String error,
        int status,
        String message
) {
}
