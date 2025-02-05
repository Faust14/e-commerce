package com.shop.product_service.mapper;

import com.shop.product_service.domain.Category;
import com.shop.product_service.dto.request.CreateCategoryRequest;
import com.shop.product_service.dto.response.CategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toCategory(CreateCategoryRequest createCategoryRequest) {
        return Category.builder()
                .name(createCategoryRequest.name())
                .build();
    }

    public CategoryResponse toCategoryResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }
}
