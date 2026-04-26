package com.nearbuy.backend.controller;

import com.nearbuy.backend.dto.order.OrderRequest;
import com.nearbuy.backend.dto.order.OrderResponse;
import com.nearbuy.backend.enums.OrderStatus;
import com.nearbuy.backend.service.CurrentUserService;
import com.nearbuy.backend.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Checkout, order history, and order status APIs")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final OrderService orderService;
    private final CurrentUserService currentUserService;

    //Checkout
    @PostMapping("/checkout")
    @Operation(summary = "Checkout current user's cart")
    public OrderResponse checkout(
            @Valid @RequestBody OrderRequest request) {

        Long userId = currentUserService.getCurrentUserId();
        return orderService.checkout(userId, request.getAddressId());
    }

    //Get user orders
    @GetMapping
    @Operation(summary = "Get current user's orders")
    public List<OrderResponse> getOrders() {
        Long userId = currentUserService.getCurrentUserId();
        return orderService.getUserOrders(userId);
    }

    @PutMapping("/{orderId}/status")
    @Operation(summary = "Update order status")
    public OrderResponse updateStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {
        return orderService.updateStatus(orderId, status.name());
    }
}
