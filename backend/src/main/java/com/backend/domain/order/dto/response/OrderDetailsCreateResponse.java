package com.backend.domain.order.dto.response;

import com.backend.domain.order.entity.OrderDetails;

/**
 * 주문 상세 정보를 클라이언트에 전달하기 위한 DTO
 */
public record OrderDetailsCreateResponse(
        Long orderItemId,
        Long menuId,
        String menuName,
        int quantity,
        int price
) {
    // Entity를 DTO로 변환하는 생성자
    public OrderDetailsCreateResponse(OrderDetails details) {
        this(
                details.getOrderItemId(),
                details.getMenu().getMenuId(),
                details.getMenuName(),
                details.getQuantity(),
                details.getOrderPrice()
        );
    }
}