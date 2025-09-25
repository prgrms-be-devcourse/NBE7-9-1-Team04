package com.backend.domain.order.dto.request;

import jakarta.validation.constraints.*;

import java.util.List;

// 주문 생성 시 요청 DTO
public record OrderCreateRequest(
        @Email String email,
        @Min(0) int amount,
        @NotEmpty List<OrderDetailsCreateRequest> items
) {
}
