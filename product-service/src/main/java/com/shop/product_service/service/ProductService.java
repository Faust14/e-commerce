package com.shop.product_service.service;

import com.shop.product_service.domain.Category;
import com.shop.product_service.domain.Product;
import com.shop.product_service.dto.request.CreateProductRequest;
import com.shop.product_service.dto.response.ProductResponse;
import com.shop.product_service.exception.NotFoundException;
import com.shop.product_service.mapper.ProductMapper;
import com.shop.product_service.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final CategoryService categoryService;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    public ProductResponse getProductById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        Product product = productOptional.orElseThrow(() -> new NotFoundException("Product not found"));
        return productMapper.toProductResponse(product);
    }

    @Transactional
    public ProductResponse createProduct(CreateProductRequest createProductRequest) {
        Product product = productMapper.toProduct(createProductRequest);
        Category category = categoryService.getCategoryById(createProductRequest.category());
        product.setCategory(category);

        return productMapper.toProductResponse(productRepository.saveAndFlush(product));
    }

    @Transactional
    public ProductResponse updateProduct(Product product) {
        Product existingProduct = productRepository.findById(product.getId()).orElseThrow(() -> new NotFoundException("Product not found"));
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setCategory(product.getCategory());

        return productMapper.toProductResponse(productRepository.saveAndFlush(existingProduct));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

}
