package com.backend.domain.order.dto;

import com.backend.domain.order.entity.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 주문 정보를 클라이언트에 전달하기 위한 DTO
 * record를 사용하여 불변 객체로 간단하게 정의합니다.
 */
public record OrderDto(
        Long orderId,
//        Long userId,
        String status,
        int totalAmount,
        LocalDateTime orderTime,
        List<OrderDetailsDto> details
) {
    // Entity를 DTO로 변환하는 생성자
    public OrderDto(Order order) {
        this(
                order.getOrderId(),
//                order.getUser_id().getUserId(), // User 엔티티에 getUserId()가 있다고 가정
                order.getOrderStatus().name(),
                order.getOrderAmount(),
                order.getOrderTime(),
                order.getOrderDetails().stream()
                        .map(OrderDetailsDto::new)
                        .collect(Collectors.toList())
        );
    }
}