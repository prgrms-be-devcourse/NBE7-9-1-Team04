package com.backend.domain.order.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record AdminOrderSummaryResponse(
        Long orderId,
        LocalDateTime orderTime,
        int orderAmount,
        String status,
        String userEmail,
        String userPhone,
        String address,
        List<OrderSummaryDetailResponse> items
) {
}
