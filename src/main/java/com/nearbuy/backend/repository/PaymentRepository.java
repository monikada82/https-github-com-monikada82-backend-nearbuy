package com.nearbuy.backend.repository;

import com.nearbuy.backend.entity.Payment;
import com.nearbuy.backend.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByOrderIdAndStatus(Long orderId, PaymentStatus status);
}
