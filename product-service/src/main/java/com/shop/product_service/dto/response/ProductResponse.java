package com.shop.product_service.dto.response;


public record ProductResponse(
        String name,
        String description,
        String categoryName
) {
}
