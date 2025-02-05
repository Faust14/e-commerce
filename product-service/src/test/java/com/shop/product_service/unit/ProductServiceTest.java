package com.shop.product_service.unit;

import com.shop.product_service.domain.Category;
import com.shop.product_service.domain.Product;
import com.shop.product_service.dto.request.CreateProductRequest;
import com.shop.product_service.dto.response.ProductResponse;
import com.shop.product_service.exception.NotFoundException;
import com.shop.product_service.mapper.ProductMapper;
import com.shop.product_service.repository.ProductRepository;
import com.shop.product_service.service.CategoryService;
import com.shop.product_service.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private Category category;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setDescription("A powerful laptop");
        product.setPrice(1000.0);
        product.setQuantity(5);
        product.setCategory(category);

        productResponse = new ProductResponse(1L, "Laptop", "A powerful laptop", 5, 1000.0, category);
    }

    @Test
    void getAllProducts_ShouldReturnProductList_WhenProductsExist() {
        when(productRepository.findAll()).thenReturn(List.of(product));
        when(productMapper.toProductResponse(product)).thenReturn(productResponse);

        List<ProductResponse> responses = productService.getAllProducts("");

        assertEquals(1, responses.size());
        assertEquals("Laptop", responses.get(0).name());
        verify(productRepository).findAll();
    }

    @Test
    void getProductById_ShouldReturnProductResponse_WhenProductExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toProductResponse(product)).thenReturn(productResponse);

        ProductResponse response = productService.getProductById(1L);

        assertNotNull(response);
        assertEquals("Laptop", response.name());
        verify(productRepository).findById(1L);
    }

    @Test
    void getProductById_ShouldThrowNotFoundException_WhenProductDoesNotExist() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.getProductById(1L));
    }

    @Test
    void createProduct_ShouldReturnProductResponse_WhenProductIsCreated() {
        CreateProductRequest request = new CreateProductRequest("Laptop", "A powerful laptop", 5, 1000.0, 1L);
        when(productMapper.toProduct(request)).thenReturn(product);
        when(categoryService.getCategoryById(1L)).thenReturn(category);
        when(productRepository.saveAndFlush(any(Product.class))).thenReturn(product);
        when(productMapper.toProductResponse(product)).thenReturn(productResponse);

        ProductResponse response = productService.createProduct(request);

        assertNotNull(response);
        assertEquals("Laptop", response.name());
        verify(productRepository).saveAndFlush(any(Product.class));
    }

    @Test
    void updateProduct_ShouldReturnUpdatedProductResponse_WhenProductExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryService.getCategoryById(1L)).thenReturn(category);
        when(productRepository.saveAndFlush(any(Product.class))).thenReturn(product);
        when(productMapper.toProductResponse(product)).thenReturn(productResponse);

        ProductResponse response = productService.updateProduct(product);

        assertNotNull(response);
        assertEquals("Laptop", response.name());
        verify(productRepository).saveAndFlush(any(Product.class));
    }

    @Test
    void updateProduct_ShouldThrowNotFoundException_WhenProductDoesNotExist() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.updateProduct(product));
    }

    @Test
    void deleteProduct_ShouldDeleteProduct_WhenProductExists() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository).deleteById(1L);
    }

    @Test
    void deleteProduct_ShouldThrowNotFoundException_WhenProductDoesNotExist() {
        when(productRepository.existsById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> productService.deleteProduct(1L));
    }

    @Test
    void reduceQuantity_ShouldReduceProductQuantity_WhenStockIsAvailable() {

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.reduceQuantity(1L, 2);

        assertEquals(3, product.getQuantity());
        verify(productRepository).save(product);
    }

    @Test
    void reduceQuantity_ShouldThrowNotFoundException_WhenProductDoesNotExist() {

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.reduceQuantity(1L, 2));
    }

    @Test
    void reduceQuantity_ShouldThrowIllegalStateException_WhenStockIsInsufficient() {

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(IllegalStateException.class, () -> productService.reduceQuantity(1L, 10));
    }
}
