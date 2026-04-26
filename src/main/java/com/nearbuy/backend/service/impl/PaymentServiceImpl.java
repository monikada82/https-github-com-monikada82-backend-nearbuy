package com.nearbuy.backend.service.impl;

import com.nearbuy.backend.dto.payment.PaymentResponse;
import com.nearbuy.backend.entity.Order;
import com.nearbuy.backend.entity.Payment;
import com.nearbuy.backend.enums.OrderStatus;
import com.nearbuy.backend.enums.PaymentStatus;
import com.nearbuy.backend.exception.BadRequestException;
import com.nearbuy.backend.exception.ResourceNotFoundException;
import com.nearbuy.backend.repository.OrderRepository;
import com.nearbuy.backend.repository.PaymentRepository;
import com.nearbuy.backend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public PaymentResponse makePayment(Long orderId, Long userId, boolean successful) {


        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getUserId().equals(userId)) {
            throw new BadRequestException("Order does not belong to current user");
        }

        if (paymentRepository.existsByOrderIdAndStatus(orderId, PaymentStatus.SUCCESS)) {
            throw new BadRequestException("Order is already paid");
        }

        PaymentStatus status = successful ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;

        Payment payment = Payment.builder()
                .orderId(orderId)
                .amount(order.getTotalAmount())
                .status(status)
                .build();

        Payment saved = paymentRepository.save(payment);

        if (status == PaymentStatus.SUCCESS) {
            order.setStatus(OrderStatus.CONFIRMED);
            orderRepository.save(order);
        }

        return mapToResponse(saved);
    }

    private PaymentResponse mapToResponse(Payment p) {
        return PaymentResponse.builder()
                .paymentId(p.getId())
                .orderId(p.getOrderId())
                .amount(p.getAmount())
                .status(p.getStatus().name())
                .build();
    }
}
