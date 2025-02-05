package com.shop.order_service.dto.response;

public record ErrorResponse(
        String error,
        int status,
        String message
) {
}
