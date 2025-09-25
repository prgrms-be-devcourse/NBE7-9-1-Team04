package com.backend.domain.order.dto.response;

public record OrderDeleteResponse(
        Long orderId,
        String message
) {
}
