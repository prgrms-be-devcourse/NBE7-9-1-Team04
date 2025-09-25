package com.backend.domain.order.entity;

/**
 * 주문 상태를 나타내는 Enum (열거형)
 * - DB에는 'CREATED', 'PAID', 'COMPLETED' , 'CANCELED' 와 같은 문자열로 저장됩니다.
 */
public enum OrderStatus {
    CREATED, // 주문 생성,결제 전
    PAID, // 결제 완료
    COMPLETED, // 배송 완료
    CANCELED; // 주문 취소

    public boolean canTransitionTo(OrderStatus newStatus) {
        return switch (this) {
            //주문 생성 후 결제 완료 또는 취소 가능
            case CREATED -> newStatus == PAID || newStatus == CANCELED;

            // 결제 완료 후: 배송 완료되기 전까지는 취소 가능
            case PAID -> newStatus == COMPLETED || newStatus == CANCELED;

            // 배송 완료 후에는 상태 변경 불가
            case COMPLETED -> false;

            // 취소된 주문은 더 이상 상태 변경 불가
            case CANCELED -> false;

            default -> false;
        };
    }
}