package com.backend.domain.payment.dto;

import com.backend.domain.payment.entity.Payment;

import java.time.LocalDateTime;

record PaymentDto(
        Long paymentId,
        int orderAmount,
        String orderPaymentType,
        String orderStatus,
        LocalDateTime createDate,
        LocalDateTime modifyDate
) {
    public PaymentDto(Payment payment) {
        this(
                payment.getPaymentId(),
                payment.getOrderAmount(),
                payment.getOrderPaymentType(),
                payment.getOrderStatus(),
                payment.getCreateDate(),
                payment.getModifyDate()
        );
    }
}