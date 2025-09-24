package com.backend.domain.payment.service;

import com.backend.domain.order.entity.Orders;
import com.backend.domain.order.repository.OrderRepository;
import com.backend.domain.payment.dto.request.PaymentCreateRequest;
import com.backend.domain.payment.dto.response.PaymentCreateResponse;
import com.backend.domain.payment.dto.response.PaymentInquiryResponse;
import com.backend.domain.payment.entity.Payment;
import com.backend.domain.payment.repository.PaymentRepository;
import com.backend.global.exception.BusinessException;
import com.backend.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PaymentService {
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    // 결제 요청
    @Transactional
    public PaymentCreateResponse createPayment(PaymentCreateRequest request) {
        Orders orders = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PAYMENT));

        Payment payment = Payment.builder()
                .paymentAmount(request.paymentAmount())
                .paymentMethod(request.paymentMethod())
                .build();

        Payment savePayment = paymentRepository.save(payment);

        // 주문 데이터에 paymentId 업데이트
//        orders.setPayment(savePayment);
//        orderRepository.save(orders);

        return new PaymentCreateResponse(savePayment);
    }

    // 결제 단건 조회
    public PaymentInquiryResponse getPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PAYMENT));

        return new PaymentInquiryResponse(payment);
    }
}
