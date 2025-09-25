package com.backend.domain.payment.repository;

import com.backend.domain.order.entity.Orders;
import com.backend.domain.payment.entity.Payment;
import com.backend.domain.payment.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByOrdersAndPaymentStatus(Orders orders, PaymentStatus paymentStatus);
}
