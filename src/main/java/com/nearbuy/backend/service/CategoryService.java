package com.nearbuy.backend.service;

import com.nearbuy.backend.dto.category.CategoryRequest;
import com.nearbuy.backend.dto.category.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse addCategory(CategoryRequest request);

    List<CategoryResponse> getCategories();

    CategoryResponse getCategory(Long categoryId);
}
