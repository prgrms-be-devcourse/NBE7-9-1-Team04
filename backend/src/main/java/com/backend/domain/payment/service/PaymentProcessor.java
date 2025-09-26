package com.backend.domain.payment.service;

import com.backend.domain.payment.dto.request.PaymentCreateRequest;

public interface PaymentProcessor {
    // 결제 처리 전략 정의
    boolean process(PaymentCreateRequest request);
}
