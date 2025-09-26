package com.backend.domain.payment.service;

import com.backend.domain.order.entity.Orders;
import com.backend.domain.payment.dto.request.PaymentCreateRequest;
import com.backend.domain.payment.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentFactory {
    // 성공/실패 시 각각의 결과를 담은 객체 생성
    public Payment createCompletedPayment(PaymentCreateRequest request, Orders orders) {
        Payment payment = request.createPayment(orders);
        payment.complete();
        return payment;
    }

    public Payment createFailedPayment(PaymentCreateRequest request, Orders orders) {
        Payment payment = request.createPayment(orders);
        payment.fail();
        return payment;
    }
}