package com.shop.product_service.dto.response;

public record ErrorResponse (
        String error,
        int status,
        String message
) {
}
