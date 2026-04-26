package com.nearbuy.backend.controller;

import com.nearbuy.backend.dto.category.CategoryRequest;
import com.nearbuy.backend.dto.category.CategoryResponse;
import com.nearbuy.backend.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Category management APIs")
@SecurityRequirement(name = "bearerAuth")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @Operation(summary = "Create a new product category")
    public CategoryResponse addCategory(@Valid @RequestBody CategoryRequest request) {
        return categoryService.addCategory(request);
    }

    @GetMapping
    @Operation(summary = "Get all product categories")
    public List<CategoryResponse> getCategories() {
        return categoryService.getCategories();
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "Get category by ID")
    public CategoryResponse getCategory(@PathVariable Long categoryId) {
        return categoryService.getCategory(categoryId);
    }
}
