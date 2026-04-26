package com.nearbuy.backend.controller;

import com.nearbuy.backend.dto.cart.CartRequest;
import com.nearbuy.backend.dto.cart.CartResponse;
import com.nearbuy.backend.service.CartService;
import com.nearbuy.backend.service.CurrentUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Logged-in user's cart APIs")
@SecurityRequirement(name = "bearerAuth")
public class CartController {

    private final CartService cartService;
    private final CurrentUserService currentUserService;


    @PostMapping
    @Operation(summary = "Add product to current user's cart")
    public CartResponse addToCart(
            @Valid @RequestBody CartRequest request) {

        Long userId = currentUserService.getCurrentUserId();
        return cartService.addToCart(userId, request);
    }


    @GetMapping
    @Operation(summary = "Get current user's cart")
    public CartResponse getCart() {
        Long userId = currentUserService.getCurrentUserId();
        return cartService.getCart(userId);
    }


    @DeleteMapping("/items/{itemId}")
    @Operation(summary = "Remove an item from current user's cart")
    public void removeFromCart(
            @PathVariable Long itemId) {

        Long userId = currentUserService.getCurrentUserId();
        cartService.removeFromCart(userId, itemId);
    }
}
