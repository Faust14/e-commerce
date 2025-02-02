package com.shop.product_service.service;

import com.shop.product_service.domain.Category;
import com.shop.product_service.dto.request.CreateCategoryRequest;
import com.shop.product_service.dto.response.CategoryResponse;
import com.shop.product_service.exception.AlreadyExistException;
import com.shop.product_service.exception.NotFoundException;
import com.shop.product_service.mapper.CategoryMapper;
import com.shop.product_service.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    public CategoryResponse getCategoryResponseById(Long id) {
        return categoryMapper.toCategoryResponse(getCategoryById(id));
    }

    Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
    }

    @Transactional
    public CategoryResponse createCategory(CreateCategoryRequest createCategoryRequest) {
        Optional<Category> existingCategory = categoryRepository.findByName(createCategoryRequest.name());

        existingCategory.ifPresent(category -> {
            throw new AlreadyExistException("Category with name '" + createCategoryRequest.name() + "' already exists.");
        });

        Category category = categoryMapper.toCategory(createCategoryRequest);
        return categoryMapper.toCategoryResponse(categoryRepository.saveAndFlush(category));
    }

    @Transactional
    public CategoryResponse updateCategory(Category category) {
        Category existingCategory = categoryRepository.findById(category.getId()).orElseThrow(() -> new NotFoundException("Category not found"));
        existingCategory.setName(category.getName());

        return categoryMapper.toCategoryResponse(categoryRepository.saveAndFlush(existingCategory));
    }

    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }


}
