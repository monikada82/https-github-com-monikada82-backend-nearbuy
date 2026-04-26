package com.nearbuy.backend.controller;

import com.nearbuy.backend.dto.payment.PaymentResponse;
import com.nearbuy.backend.service.CurrentUserService;
import com.nearbuy.backend.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Mock payment APIs for orders")
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {

    private final PaymentService paymentService;
    private final CurrentUserService currentUserService;

    @PostMapping("/{orderId}")
    @Operation(summary = "Make a mock payment for an order")
    public PaymentResponse makePayment(
            @PathVariable Long orderId,
            @RequestParam(defaultValue = "true") boolean successful) {

        Long userId = currentUserService.getCurrentUserId();
        return paymentService.makePayment(orderId, userId, successful);
    }
}
