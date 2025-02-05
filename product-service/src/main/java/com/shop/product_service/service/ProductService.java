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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final CategoryService categoryService;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductResponse> getAllProducts(String search) {
        List<Product> products;
        if (search == null || search.trim().isEmpty()) {
            products = productRepository.findAll();
        } else {
            products = productRepository.findByNameContainingIgnoreCase(search);
        }
        return products.stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
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
    public ProductResponse updateProduct(Product newProduct) {
        Product product = productRepository.findById(newProduct.getId()).orElseThrow(() -> new NotFoundException("Product not found"));
        product.setName(newProduct.getName());
        product.setDescription(newProduct.getDescription());
        product.setQuantity(newProduct.getQuantity());
        product.setPrice(newProduct.getPrice());
        Category category = categoryService.getCategoryById(newProduct.getId());
        product.setCategory(category);
        productRepository.saveAndFlush(product);
        return productMapper.toProductResponse(product);
    }

    @Transactional
    public void reduceQuantity(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getQuantity() < quantity) {
            throw new IllegalStateException("Not enough stock for product: " + product.getName());
        }

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new NotFoundException("Product not found");
        }
        productRepository.deleteById(id);
    }

}
