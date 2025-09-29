package com.backend.domain.payment.service;

import com.backend.domain.payment.dto.request.PaymentCreateRequest;
import org.springframework.stereotype.Service;

@Service
public class RandomPaymentProcessor implements PaymentProcessor {
    @Override
    public boolean process(PaymentCreateRequest request) {
        /*
         * 결제 실패 시뮬레이션: 50% 성공률
         */
        return Math.random() > 0.5;
    }
}
