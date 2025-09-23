package com.backend.domain.payment.dto;

record PaymentDto(
        Long paymentId,
        int orderAmount,
        String orderPaymentType,
        String orderStatus
) {}