package com.shop.product_service.service;

import com.shop.product_service.domain.Product;
import com.shop.product_service.dto.request.CreateProductRequest;
import com.shop.product_service.exception.NotFoundException;
import com.shop.product_service.mapper.ProductMapper;
import com.shop.product_service.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public Product createProduct(CreateProductRequest createProductRequest) {
        Product product = productMapper.toProduct(createProductRequest);
        return productRepository.saveAndFlush(product);
    }

    @Transactional
    public Product updateProduct(Product product) {
        Product existingProduct = productRepository.findById(product.getId()).orElseThrow(()->new NotFoundException("Product not found"));
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setCategory(product.getCategory());

        return productRepository.saveAndFlush(existingProduct);
    }
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
