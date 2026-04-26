package com.nearbuy.backend.controller;

import com.nearbuy.backend.dto.product.ProductRequest;
import com.nearbuy.backend.dto.product.ProductResponse;
import com.nearbuy.backend.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product creation, search, filtering, pagination, and sorting APIs")
@SecurityRequirement(name = "bearerAuth")
public class ProductController {
    public final ProductService productService;

    @PostMapping
    @Operation(summary = "Create a new product")
    public ProductResponse addProduct(@Valid @RequestBody ProductRequest request){
        return productService.addProduct(request);
    }

    @GetMapping({"", "/search"})
    @Operation(summary = "Search and filter products with pagination")
    public Page<ProductResponse> getProducts(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") double min,
            @RequestParam(defaultValue = "1000000") double max,
            Pageable pageable) {

        return productService.getProducts(keyword, min, max, pageable);
    }

    @GetMapping("/filter")
    @Operation(summary = "Filter products by price range")
    public Page<ProductResponse>filterByPrice(
                @RequestParam double min,
                @RequestParam double max,
                Pageable pageable
                ){
            return productService.filterProductsByPrice(min,max,pageable);
        }
    }

