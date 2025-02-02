package com.shop.product_service.dto.response;

public record ProductResponse(
        Long id,
        String name,
        String description,
        int quantity,
        Double price,
        String categoryName
) {
}
