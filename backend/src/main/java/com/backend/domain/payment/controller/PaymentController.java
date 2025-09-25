package com.backend.domain.payment.controller;

import com.backend.domain.payment.dto.request.PaymentCreateRequest;
import com.backend.domain.payment.dto.response.PaymentCreateResponse;
import com.backend.domain.payment.dto.response.PaymentInquiryResponse;
import com.backend.domain.payment.service.PaymentService;
import com.backend.global.response.ApiResponse;
import jakarta.validation.Valid;
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
            @Valid @RequestBody PaymentCreateRequest request
            ) {
        PaymentCreateResponse response = paymentService.createPayment(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 결제 단건 조회 API
    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse<PaymentInquiryResponse>> getPayment(
            @Valid @PathVariable Long paymentId
    ) {
        PaymentInquiryResponse response = paymentService.getPayment(paymentId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
