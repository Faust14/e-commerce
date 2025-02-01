package com.shop.product_service.mapper;

import com.shop.product_service.domain.Product;
import com.shop.product_service.dto.request.CreateProductRequest;
import com.shop.product_service.dto.response.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toProduct(CreateProductRequest createProductRequest) {

        return Product.builder()
                .name(createProductRequest.name())
                .description(createProductRequest.description())
                .build();
    }


    public ProductResponse toProductResponse(Product product) {
        return new ProductResponse(
                product.getName(),
                product.getDescription(),
                product.getCategory().getName()
        );
    }
}
