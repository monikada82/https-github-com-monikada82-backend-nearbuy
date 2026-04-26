package com.nearbuy.backend.service.impl;

import com.nearbuy.backend.dto.product.ProductRequest;
import com.nearbuy.backend.dto.product.ProductResponse;
import com.nearbuy.backend.entity.Category;
import com.nearbuy.backend.entity.Product;
import com.nearbuy.backend.exception.ResourceNotFoundException;
import com.nearbuy.backend.repository.CategoryRepository;
import com.nearbuy.backend.repository.ProductRepository;
import com.nearbuy.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;


    @Override
    public ProductResponse addProduct(ProductRequest request){

        log.info("Adding product: {}", request.getName());

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .category(category)
                .build();

        Product saved = productRepository.save(product);

        log.info("Product saved with ID: {}", saved.getId());

        return mapToResponse(saved);
    }


    @Override
    public Page<ProductResponse> getProducts(String keyword, double min, double max, Pageable pageable) {

        log.info("Fetching products with keyword: {}, min: {}, max: {}", keyword, min, max);

        return productRepository
                .findByNameContainingIgnoreCaseAndPriceBetween(
                        keyword, min, max, pageable)
                .map(this::mapToResponse);
    }


    @Override
    public Page<ProductResponse> searchProducts(String keyword, Pageable pageable) {

        log.info("Searching products with keyword: {}", keyword);

        return productRepository
                .findByNameContainingIgnoreCase(keyword, pageable)
                .map(this::mapToResponse);
    }


    @Override
    public Page<ProductResponse> filterProductsByPrice(double min, double max, Pageable pageable) {

        log.info("Filtering products between {} and {}", min, max);

        return productRepository
                .findByPriceBetween(min, max, pageable)
                .map(this::mapToResponse);
    }


    private ProductResponse mapToResponse(Product p) {
        return ProductResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .price(p.getPrice())
                .stock(p.getStock())
                .categoryId(p.getCategory() == null ? null : p.getCategory().getId())
                .categoryName(p.getCategory() == null ? null : p.getCategory().getName())
                .build();
    }
}
