package com.shop.product_service.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreateProductRequest(
        @NotNull
        String name,
        @NotNull
        String description,
        @NotNull
        int quantity,
        @NotNull
        Double price,
        @NotNull
        Long category
) {
}
