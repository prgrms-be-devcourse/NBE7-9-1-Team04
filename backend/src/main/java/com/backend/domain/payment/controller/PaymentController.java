package com.backend.domain.payment.controller;

import com.backend.domain.payment.dto.request.PaymentCreateRequest;
import com.backend.domain.payment.dto.response.PaymentCancelResponse;
import com.backend.domain.payment.dto.response.PaymentCreateResponse;
import com.backend.domain.payment.dto.response.PaymentInquiryResponse;
import com.backend.domain.payment.service.PaymentService;
import com.backend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Payment", description = "결제 API")
public class PaymentController {
    private final PaymentService paymentService;

    // 결제 생성 API
    @Operation(
            summary = "결제 생성 및 처리",
            description = "주문에 대한 결제를 생성하고 MOCK 방식으로 즉시 승인 처리합니다. " +
                    "결제 완료 시 주문 상태가 PAID로 변경됩니다."
    )
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PaymentCreateResponse>> createPayment(
            @Valid @RequestBody PaymentCreateRequest request
            ) {
        PaymentCreateResponse response = paymentService.createPayment(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 결제 단건 조회 API
    @Operation(
            summary = "결제 상세 정보 조회",
            description = "결제 ID를 통해 결제의 상세 정보(금액, 방법, 상태, 생성일시 등)를 조회합니다."
    )
    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse<PaymentInquiryResponse>> getPayment(
            @Valid @PathVariable Long paymentId
    ) {
        PaymentInquiryResponse response = paymentService.getPayment(paymentId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 결제 단건 삭제 API
    @Operation(
            summary = "결제 취소",
            description = "완료(COMPLETED)된 결제를 취소(CANCELED) 상태로 변경합니다. " +
                    "이미 취소된 결제나 취소 불가능한 상태의 결제는 에러를 반환합니다."
    )
    @PutMapping("/{paymentId}/cancel")
    public ResponseEntity<ApiResponse<PaymentCancelResponse>> cancelPayment(
            @Valid @PathVariable Long paymentId
    ) {
        PaymentCancelResponse response = paymentService.cancelPayment(paymentId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
