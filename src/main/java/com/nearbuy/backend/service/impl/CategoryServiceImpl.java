package com.nearbuy.backend.service.impl;

import com.nearbuy.backend.dto.category.CategoryRequest;
import com.nearbuy.backend.dto.category.CategoryResponse;
import com.nearbuy.backend.entity.Category;
import com.nearbuy.backend.exception.BadRequestException;
import com.nearbuy.backend.exception.ResourceNotFoundException;
import com.nearbuy.backend.repository.CategoryRepository;
import com.nearbuy.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse addCategory(CategoryRequest request) {
        if (categoryRepository.findByNameIgnoreCase(request.getName()).isPresent()) {
            throw new BadRequestException("Category already exists");
        }

        Category category = Category.builder()
                .name(request.getName().trim())
                .build();

        return mapToResponse(categoryRepository.save(category));
    }

    @Override
    public List<CategoryResponse> getCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CategoryResponse getCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        return mapToResponse(category);
    }

    private CategoryResponse mapToResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
