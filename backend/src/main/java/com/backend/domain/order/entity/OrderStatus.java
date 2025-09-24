package com.backend.domain.order.entity;

/**
 * 주문 상태를 나타내는 Enum (열거형)
 * - DB에는 'PAYMENT_PENDING', 'PAYMENT_COMPLETED', 'SHIPPING_COMPLETED' 와 같은 문자열로 저장됩니다.
 */
public enum OrderStatus {
    PAYMENT_PENDING, // 결제 전
    PAYMENT_COMPLETED, // 결제 완료
    SHIPPING_COMPLETED // 배송 완료
}