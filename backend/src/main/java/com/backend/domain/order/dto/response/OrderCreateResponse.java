package com.backend.domain.order.dto.response;

import com.backend.domain.order.entity.Orders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 주문 정보를 클라이언트에 전달하기 위한 DTO
 * record를 사용하여 불변 객체로 간단하게 정의합니다.
 */
public record OrderCreateResponse(
        Long orderId,
        Long userId,
        String status,
        int orderAmount,
        LocalDateTime orderTime,
        List<OrderDetailsCreateResponse> details
) {
    // Entity를 DTO로 변환하는 생성자
    public OrderCreateResponse(Orders order) {
        this(
                order.getOrderId(),
                order.getUser().getUserId(),
                order.getOrderStatus().name(),
                order.getOrderAmount(),
                order.getCreateDate(),
                order.getOrderDetails().stream()
                        .map(OrderDetailsCreateResponse::new)
                        .collect(Collectors.toList())
        );
    }

    public OrderCreateResponse(Optional<Orders> updatedOrder) {
        this(updatedOrder.orElseThrow(() -> new IllegalArgumentException("Order not found")));
    }
}