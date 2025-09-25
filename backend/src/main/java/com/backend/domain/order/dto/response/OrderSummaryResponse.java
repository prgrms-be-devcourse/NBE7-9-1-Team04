package com.backend.domain.order.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record OrderSummaryResponse(
        Long orderId,              // 주문 번호
        LocalDateTime orderTime,   // 주문 일시
        int orderAmount,           // 총 금액
        String status,             // 주문 상태
        String address,            // 주소 (Address 엔티티의 필드 합쳐서 String 처리)
        List<OrderSummaryDetailResponse> items  // 주문 상세 목록
) {}