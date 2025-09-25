package com.backend.domain.payment.dto.response;

import com.backend.domain.payment.entity.Payment;
import com.backend.domain.payment.entity.PaymentMethod;
import com.backend.domain.payment.entity.PaymentStatus;

import java.time.LocalDateTime;

public record PaymentInquiryResponse(
        Long paymentId,
        int paymentAmount,
        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus,
        LocalDateTime createDate,
        LocalDateTime modifyDate
) {
    public PaymentInquiryResponse(Payment payment) {
        this(
                payment.getPaymentId(),
                payment.getPaymentAmount(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus(),
                payment.getCreateDate(),
                payment.getModifyDate()
        );
    }
}
