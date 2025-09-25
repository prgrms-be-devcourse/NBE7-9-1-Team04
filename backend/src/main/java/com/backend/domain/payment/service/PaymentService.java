package com.backend.domain.payment.service;

import com.backend.domain.order.entity.OrderStatus;
import com.backend.domain.order.entity.Orders;
import com.backend.domain.order.repository.OrderRepository;
import com.backend.domain.payment.dto.request.PaymentCreateRequest;
import com.backend.domain.payment.dto.response.PaymentCreateResponse;
import com.backend.domain.payment.dto.response.PaymentInquiryResponse;
import com.backend.domain.payment.entity.Payment;
import com.backend.domain.payment.entity.PaymentMethod;
import com.backend.domain.payment.entity.PaymentStatus;
import com.backend.domain.payment.repository.PaymentRepository;
import com.backend.global.exception.BusinessException;
import com.backend.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class PaymentService {
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    // 결제 요청
    @Transactional
    public PaymentCreateResponse createPayment(PaymentCreateRequest request) {
        Orders orders = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ORDER));

        if(orders.getOrderAmount() != request.paymentAmount()){
            throw new BusinessException(ErrorCode.PAYMENT_AMOUNT_MISMATCH);
        }

        if(request.paymentMethod() == null || request.paymentMethod() != PaymentMethod.CARD) {
            throw new BusinessException(ErrorCode.INVALID_PAYMENT_METHOD);
        }

        boolean completePayment = paymentRepository.existsByOrdersAndPaymentStatus(orders, PaymentStatus.COMPLETED);
        if(completePayment) {
            throw new BusinessException(ErrorCode.PAYMENT_ALREADY_COMPLETED);
        }

        Payment payment = request.createPayment(orders);
        Payment savePayment = paymentRepository.save(payment);

        orders.setPayment(savePayment);
        orders.setOrderStatus(OrderStatus.PAID);
        orderRepository.save(orders);

        payment.complete();

        return new PaymentCreateResponse(savePayment);
    }

    // 결제 단건 조회
    public PaymentInquiryResponse getPayment(Long paymentId) {
        if(paymentId == null || paymentId <= 0) {
            throw new BusinessException(ErrorCode.NOT_FOUND_PAYMENT);
        }

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PAYMENT));

        return new PaymentInquiryResponse(payment);
    }
}
