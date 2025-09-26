package com.backend.domain.order.dto.response;

public record OrderSummaryDetailResponse(
        String productName,   // 상품명
        int quantity,         // 상품 개수
        int orderPrice,       // 결제 금액 (해당 상품 총 금액),
        String imageUrl
) {
}
