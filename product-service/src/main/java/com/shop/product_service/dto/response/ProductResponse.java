package com.shop.product_service.dto.response;

import com.shop.product_service.domain.Category;

public record ProductResponse(
        Long id,
        String name,
        String description,
        int quantity,
        Double price,
        Category category
) {
}
