package com.backend.domain.order.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// 주문 생성시 상세 요청 DTO
public record OrderDetailsCreateRequest(
        @NotNull Long productId,
        @NotBlank String menuName,
        @Min(0) int quantity,
        @Min(0) int orderPrice
) {
}
