package com.nearbuy.backend.dto.cart;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemResponse {

    private Long cartItemId;
    private Long productId;
    private String productName;
    private int quantity;
    private double price;
    private double lineTotal;
}
