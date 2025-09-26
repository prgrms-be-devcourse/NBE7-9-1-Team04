package com.backend.domain.payment.service;

import com.backend.domain.order.entity.Orders;
import com.backend.domain.order.repository.OrderRepository;
import com.backend.domain.order.service.OrderService;
import com.backend.domain.payment.dto.request.PaymentCreateRequest;
import com.backend.domain.payment.dto.response.PaymentCancelResponse;
import com.backend.domain.payment.dto.response.PaymentCreateResponse;
import com.backend.domain.payment.dto.response.PaymentInquiryResponse;
import com.backend.domain.payment.entity.Payment;
import com.backend.domain.payment.entity.PaymentMethod;
import com.backend.domain.payment.entity.PaymentStatus;
import com.backend.domain.payment.repository.PaymentRepository;
import com.backend.domain.user.user.dto.UserDto;
import com.backend.global.exception.BusinessException;
import com.backend.global.response.ErrorCode;
import jakarta.validation.Valid;
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
    private final OrderService orderService;

    // 결제 요청
    @Transactional
    public PaymentCreateResponse createPayment(PaymentCreateRequest request, UserDto currentUser) {
        Orders orders = orderRepository.findByOrderIdAndUser_UserId(request.orderId(), currentUser.userId())
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

        /**
         * 결제 시도 로직: 성공 시 적재, 실패 시 재시도 1회
         * */
        int maxRetries = 2;
        Exception lastException = null;
        for(int attempt = 1; attempt <= maxRetries; attempt++){
            try {
                boolean paymentSuccess = processPayment(request);
                if(paymentSuccess) {
                    Payment payment = createCompletedPayment(request, orders);
                    Payment savePayment = paymentRepository.save(payment);

                    updateOrderForSuccessfulPayment(orders, savePayment);

                    return new PaymentCreateResponse(savePayment);
                }
            } catch (Exception e) {
                lastException = e;
                log.error("결제 시도 중 오류: error={}", e.getMessage());
            }
        }

        /**
         * 결제 처리가 최종 실패했을 경우: 실패 데이터 적재
         * */
        Payment failedPayment = createFailedPayment(request, orders);
        paymentRepository.save(failedPayment);

        throw new BusinessException(ErrorCode.PAYMENT_FAILED);
    }

    // 결제 처리 (결제 성공 여부 확인)
    private boolean processPayment(PaymentCreateRequest request){
        try {
            /*
            * 결제 실패 시뮬레이션을 위한 확률, 70% 성공률이라고 가정
            * */
            boolean success = Math.random() > 0.3;

            return success;
        } catch (Exception e){
            return false;
        }
    }

    // 결제 성공 시 결제 상태가 COMPLETE 상태의 Payment 생성
    private Payment createCompletedPayment(PaymentCreateRequest request, Orders orders){
        Payment payment = request.createPayment(orders);
        payment.complete();
        return payment;
    }

    // 결제 실패 시 결제 상태가 FAILD 상태인 Payment 생성
    private Payment createFailedPayment(PaymentCreateRequest request, Orders orders){
        Payment payment = request.createPayment(orders);
        payment.fail();
        return payment;
    }

    // 결제 성공 시 주문 상태 업데이트
    private void updateOrderForSuccessfulPayment(Orders orders, Payment payment) {
        orders.setPayment(payment);
        orderService.updateOrderStatus(orders.getOrderId(), "PAID");
    }

    // 결제 단건 조회
    public PaymentInquiryResponse getPayment(Long paymentId, UserDto currentUser) {
        if(paymentId == null || paymentId <= 0) {
            throw new BusinessException(ErrorCode.NOT_FOUND_PAYMENT);
        }

        Payment payment = paymentRepository.findByPaymentIdAndOrders_User_UserId(paymentId, currentUser.userId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PAYMENT));

        return new PaymentInquiryResponse(payment);
    }

    // 결제 단건 취소
    @Transactional
    public PaymentCancelResponse cancelPayment(@Valid Long paymentId, UserDto currentUser) {
        if(paymentId == null || paymentId <= 0) {
            throw new BusinessException(ErrorCode.NOT_FOUND_PAYMENT);
        }

        Payment payment = paymentRepository.findByPaymentIdAndOrders_User_UserId(paymentId, currentUser.userId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PAYMENT));

        if(payment.getPaymentStatus() == PaymentStatus.CANCELED) {
            throw new BusinessException(ErrorCode.PAYMENT_ALREADY_CANCELLED);
        }

        if(payment.getPaymentStatus() != PaymentStatus.COMPLETED) {
            throw new BusinessException(ErrorCode.PAYMENT_NOT_CANCELLABLE);
        }

        payment.cancel();

        return new PaymentCancelResponse(payment);
    }

    // 취소된 결제 내역 삭제
    @Transactional
    public void deletePayment(@Valid Long paymentId, UserDto currentUser) {
        if(paymentId == null || paymentId <= 0) {
            throw new BusinessException(ErrorCode.NOT_FOUND_PAYMENT);
        }

        Payment payment = paymentRepository.findByPaymentIdAndOrders_User_UserId(paymentId, currentUser.userId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PAYMENT));

        if(payment.getPaymentStatus() != PaymentStatus.CANCELED) {
            throw new BusinessException(ErrorCode.PAYMENT_DELETE_FAILED);
        }

        if(payment.getOrders() != null) {
            payment.getOrders().setPayment(null);
        }

        paymentRepository.delete(payment);
    }
}
