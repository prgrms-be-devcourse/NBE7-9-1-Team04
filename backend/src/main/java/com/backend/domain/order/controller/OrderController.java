package com.backend.domain.order.controller;

import com.backend.domain.order.dto.OrderDto;
import com.backend.domain.order.entity.Orders;
import com.backend.domain.order.service.OrderService;
import com.backend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "OrderController", description = "주문 API")
public class OrderController {

    private final OrderService orderService;

    // 주문 상세 요청 DTO
    public record OrderItemRequest(
            @NotNull Long productId,
            @NotBlank String menuName,
            @Min(1) int quantity,
            @Min(0) int orderPrice
    ) {
    }

    // 주문 생성 요청 DTO
    public record OrderCreateRequest(
            @Email String email,
            @Min(0) int amount,
            @NotEmpty List<OrderItemRequest> items
    ) { }


    @PostMapping
    @Transactional
    @Operation(summary = "주문 생성")
    public ResponseEntity<ApiResponse<OrderDto>> createOrder(
            @Valid @RequestBody OrderCreateRequest request
    ) {
        Orders order = orderService.createOrder(request);

        return ResponseEntity.ok(ApiResponse.success(new OrderDto(order)));
    }

    public record OrderStatusUpdateReqBody(
            @NotNull
            String newStatus
    ) {
    }

    @PutMapping("/{orderId}/status")
    @Transactional
    @Operation(summary = "주문 상태 업데이트")
    public ResponseEntity<ApiResponse<OrderDto>> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody @Valid OrderStatusUpdateReqBody reqBody

    ) {
        // 주문 상태 업데이트 로직 (예: 결제 완료, 배송 중 등)
        orderService.updateOrderStatus(orderId, reqBody.newStatus());

        // 업데이트된 주문 정보 반환
        Optional<Orders> updatedOrder = orderService.getOrderById(orderId);
        return ResponseEntity.ok(ApiResponse.success(new OrderDto(updatedOrder)));
    }

    public record OrderSummaryResponse(
            Long orderId,
            String status,
            int orderAmount,
            LocalDateTime orderTime
    ) {
    }

    @GetMapping
    @Operation(summary = "사용자 주문 목록 조회")
    public ResponseEntity<ApiResponse<List<OrderSummaryResponse>>> getOrders(
            @RequestParam Long userId
    ) {
        List<Orders> orders = orderService.getOrdersByUserId(userId);

        // Orders → OrderSummaryResponse 변환
        List<OrderSummaryResponse> summaries = orders.stream()
                .map(order -> new OrderSummaryResponse(
                        order.getOrderId(),
                        order.getOrderStatus().name(),
                        order.getOrderAmount(),
                        order.getCreateDate()
                ))
                .toList();

        return ResponseEntity.ok(ApiResponse.success(summaries));
    }
}
