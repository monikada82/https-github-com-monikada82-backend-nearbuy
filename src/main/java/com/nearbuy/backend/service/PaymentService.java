package com.nearbuy.backend.service;

import com.nearbuy.backend.dto.payment.PaymentResponse;

public interface PaymentService {

    PaymentResponse makePayment(Long orderId, Long userId, boolean successful);
}
