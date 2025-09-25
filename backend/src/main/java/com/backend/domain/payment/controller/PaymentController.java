package com.backend.domain.payment.controller;

import com.backend.domain.payment.dto.request.PaymentCreateRequest;
import com.backend.domain.payment.dto.response.PaymentCreateResponse;
import com.backend.domain.payment.dto.response.PaymentInquiryResponse;
import com.backend.domain.payment.service.PaymentService;
import com.backend.global.response.ApiResponse;
import com.backend.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;

    // 결제 생성 API
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PaymentCreateResponse>> createPayment(
            @RequestBody PaymentCreateRequest request
            ) {
        try {
            PaymentCreateResponse response = paymentService.createPayment(request);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            log.error("결제 요청 API 시스템 오류", e);
            return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.NOT_FOUND_PAYMENT));
        }
    }

    // 결제 단건 조회 API
    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse<PaymentInquiryResponse>> getPayment(
            @PathVariable Long paymentId
    ) {
        try {
            PaymentInquiryResponse response = paymentService.getPayment(paymentId);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            log.error("결제 단건 조회 API 시스템 오류", e);
            // 본인 것만 조회 가능하게 예외 처리 수정
            return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.NOT_FOUND_PAYMENT));
        }
    }
}
