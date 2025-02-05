package com.shop.product_service.controller;

import com.shop.product_service.domain.Product;
import com.shop.product_service.dto.request.CreateProductRequest;
import com.shop.product_service.dto.response.ProductResponse;
import com.shop.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(@RequestParam(value = "search", required = false, defaultValue = "") String search) {
        return ResponseEntity.ok(productService.getAllProducts(search.trim()));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody CreateProductRequest createProductRequest) {
        ProductResponse response = productService.createProduct(createProductRequest);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<ProductResponse> updateProduct(@RequestBody Product product) {
        ProductResponse response = productService.updateProduct(product);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/{productId}/reduceQuantity")
    public ResponseEntity<Void> reduceProductQuantity(@PathVariable Long productId, @RequestParam int quantity) {
        log.info("Reducing quantity for productId: {}, quantity: {}", productId, quantity);
        productService.reduceQuantity(productId, quantity);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}