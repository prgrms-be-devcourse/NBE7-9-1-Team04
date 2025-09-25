package com.backend.domain.order.entity;

/**
 * 주문 상태를 나타내는 Enum (열거형)
 * - DB에는 'PAYMENT_PENDING', 'PAYMENT_COMPLETED', 'SHIPPING_COMPLETED' 와 같은 문자열로 저장됩니다.
 */
public enum OrderStatus {
    CREATED, // 주문 생성,결제 전
    PAID, // 결제 완료
    COMPLETED, // 배송 완료
    CANCELED;

    public boolean canTransitionTo(OrderStatus newStatus) {
        return switch (this) {
            case CREATED -> newStatus == PAID; // 결제 전 -> 결제 완료
            case PAID -> newStatus == COMPLETED; // 결제 완료 -> 배송 완료
            case COMPLETED -> false; // 배송 완료 후에는 상태 변경 불가
            default -> false;
        };
    }
}