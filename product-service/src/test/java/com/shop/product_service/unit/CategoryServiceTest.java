package com.shop.product_service.unit;

import com.shop.product_service.domain.Category;
import com.shop.product_service.dto.request.CreateCategoryRequest;
import com.shop.product_service.dto.response.CategoryResponse;
import com.shop.product_service.exception.AlreadyExistException;
import com.shop.product_service.exception.NotFoundException;
import com.shop.product_service.mapper.CategoryMapper;
import com.shop.product_service.repository.CategoryRepository;
import com.shop.product_service.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;
    private CategoryResponse categoryResponse;
    private CreateCategoryRequest createCategoryRequest;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        categoryResponse = new CategoryResponse(1L, "Electronics");

        createCategoryRequest = new CreateCategoryRequest("Electronics");
    }

    @Test
    void getAllCategories_ShouldReturnCategoryResponses_WhenCategoriesExist() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category));
        when(categoryMapper.toCategoryResponse(category)).thenReturn(categoryResponse);

        List<CategoryResponse> result = categoryService.getAllCategories(null);

        assertEquals(1, result.size());
        assertEquals("Electronics", result.get(0).name());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void getCategoryResponseById_ShouldReturnCategoryResponse_WhenCategoryExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toCategoryResponse(category)).thenReturn(categoryResponse);

        CategoryResponse result = categoryService.getCategoryResponseById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Electronics", result.name());
    }

    @Test
    void getCategoryById_ShouldThrowNotFoundException_WhenCategoryDoesNotExist() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> categoryService.getCategoryById(1L));
    }

    @Test
    void createCategory_ShouldThrowAlreadyExistException_WhenCategoryAlreadyExists() {
        when(categoryRepository.findByName(createCategoryRequest.name())).thenReturn(Optional.of(category));

        assertThrows(AlreadyExistException.class, () -> categoryService.createCategory(createCategoryRequest));
    }

    @Test
    void createCategory_ShouldReturnCategoryResponse_WhenCategoryIsCreated() {
        when(categoryRepository.findByName(createCategoryRequest.name())).thenReturn(Optional.empty());
        when(categoryMapper.toCategory(createCategoryRequest)).thenReturn(category);
        when(categoryRepository.saveAndFlush(category)).thenReturn(category);
        when(categoryMapper.toCategoryResponse(category)).thenReturn(categoryResponse);

        CategoryResponse result = categoryService.createCategory(createCategoryRequest);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Electronics", result.name());
    }

    @Test
    void updateCategory_ShouldThrowNotFoundException_WhenCategoryDoesNotExist() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> categoryService.updateCategory(category));
    }

    @Test
    void updateCategory_ShouldReturnUpdatedCategoryResponse_WhenCategoryExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.saveAndFlush(category)).thenReturn(category);
        when(categoryMapper.toCategoryResponse(category)).thenReturn(categoryResponse);

        CategoryResponse result = categoryService.updateCategory(category);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Electronics", result.name());
    }

    @Test
    void deleteCategory_ShouldCallDeleteById_WhenCategoryExists() {
        when(categoryRepository.existsById(1L)).thenReturn(true);
        doNothing().when(categoryRepository).deleteById(1L);

        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteCategory_ShouldThrowNotFoundException_WhenCategoryDoesNotExist() {
        when(categoryRepository.existsById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> categoryService.deleteCategory(1L));
    }
}
