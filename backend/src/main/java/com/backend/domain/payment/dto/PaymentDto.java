package com.backend.domain.payment.dto;

import java.time.LocalDateTime;

record PaymentDto(
        Long paymentId,
        int orderAmount,
        String orderPaymentType,
        String orderStatus,
        LocalDateTime createDate,
        LocalDateTime modifyDate
) {}