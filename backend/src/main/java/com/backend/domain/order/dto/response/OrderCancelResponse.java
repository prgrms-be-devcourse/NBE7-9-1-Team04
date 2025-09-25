package com.backend.domain.order.dto.response;

public record OrderCancelResponse(
        Long orderId,
        String message,
        String status
) {
}
