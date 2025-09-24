package com.backend.domain.payment.dto.request;

import com.backend.domain.order.entity.Orders;
import com.backend.domain.payment.entity.Payment;

record PaymentRequestDto (
        int paymentAmount,
        String paymentMethod
) {
    public Payment createPayment(Orders orders) {
        return new Payment(paymentAmount, paymentMethod, orders);
    }
}
