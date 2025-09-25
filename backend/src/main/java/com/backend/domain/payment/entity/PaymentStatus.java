package com.backend.domain.payment.entity;

/**
 * 결제 상태를 나타내는 enum
 */
public enum PaymentStatus {
    PENDING("결제 전"),
    COMPLETED("결제 완료"),
    FAILED("결제 실패"),
    CANCELLED("결제 취소");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
