package com.nearbuy.backend.service.impl;

import com.nearbuy.backend.dto.cart.CartItemResponse;
import com.nearbuy.backend.dto.cart.CartRequest;
import com.nearbuy.backend.dto.cart.CartResponse;
import com.nearbuy.backend.entity.Cart;
import com.nearbuy.backend.entity.CartItem;
import com.nearbuy.backend.entity.Product;
import com.nearbuy.backend.exception.BadRequestException;
import com.nearbuy.backend.exception.ResourceNotFoundException;
import com.nearbuy.backend.repository.CartRepository;
import com.nearbuy.backend.repository.ProductRepository;
import com.nearbuy.backend.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    // ADD TO CART
    @Override
    public CartResponse addToCart(Long userId, CartRequest request) {

        // validation
        if (request.getQuantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }

        // 1 Get or create cart
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> Cart.builder()
                        .userId(userId)
                        .items(new ArrayList<>())
                        .build());

        // 2️ Get product
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // 3️ Check existing item
        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        int requestedQuantity = request.getQuantity();
        int currentQuantity = existingItem == null ? 0 : existingItem.getQuantity();

        if (currentQuantity + requestedQuantity > product.getStock()) {
            throw new BadRequestException("Requested quantity exceeds available stock");
        }

        if (existingItem != null) {
            //  update quantity
            existingItem.setQuantity(currentQuantity + requestedQuantity);
        } else {
            // create new item
            CartItem newItem = CartItem.builder()
                    .product(product)
                    .quantity(requestedQuantity)
                    .cart(cart)
                    .build();

            cart.getItems().add(newItem);
        }

        // 4️ Save
        Cart savedCart = cartRepository.save(cart);

        return mapToResponse(savedCart);
    }

    //  GET CART
    @Override
    public CartResponse getCart(Long userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        return mapToResponse(cart);
    }

    // REMOVE ITEM (SECURE)
    @Override
    public void removeFromCart(Long userId, Long cartItemId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        cart.getItems().remove(item);

        cartRepository.save(cart);
    }

    // MAPPER
    private CartResponse mapToResponse(Cart cart) {

        List<CartItemResponse> items = cart.getItems().stream()
                .map(item -> CartItemResponse.builder()
                        .cartItemId(item.getId())
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .price(item.getProduct().getPrice())
                        .lineTotal(item.getProduct().getPrice() * item.getQuantity())
                        .build())
                .toList();

        double total = cart.getItems().stream()
                .mapToDouble(item ->
                        item.getProduct().getPrice() * item.getQuantity()
                )
                .sum();

        return CartResponse.builder()
                .cartId(cart.getId())
                .items(items)
                .totalPrice(total)
                .build();
    }
}
