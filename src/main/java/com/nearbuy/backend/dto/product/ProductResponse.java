package com.nearbuy.backend.dto.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private double price;
    private int stock;
    private Long categoryId;
    private String categoryName;
}
