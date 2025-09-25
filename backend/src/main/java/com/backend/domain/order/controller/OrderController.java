package com.backend.domain.order.controller;

import com.backend.domain.order.dto.request.OrderCreateRequest;
import com.backend.domain.order.dto.request.OrderStatusUpdateRequest;
import com.backend.domain.order.dto.response.OrderCreateResponse;
import com.backend.domain.order.dto.response.OrderSummaryDetailResponse;
import com.backend.domain.order.dto.response.OrderSummaryResponse;
import com.backend.domain.order.entity.Orders;
import com.backend.domain.order.service.OrderService;
import com.backend.domain.user.address.entity.Address;
import com.backend.domain.user.address.repository.AddressRepository;
import com.backend.domain.user.address.service.AddressService;
import com.backend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    @PostMapping
    @Transactional
    @Operation(summary = "주문 생성")
    public ResponseEntity<ApiResponse<OrderCreateResponse>> createOrder(
            @Valid @RequestBody OrderCreateRequest request
    ) throws Exception {
        Orders order = orderService.createOrder(request);
        return ResponseEntity.ok(ApiResponse.success(new OrderCreateResponse((order))));
    }

    @PutMapping("/{orderId}/status")
    @Transactional
    @Operation(summary = "주문 상태 업데이트")
    public ResponseEntity<ApiResponse<OrderCreateResponse>> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody @Valid OrderStatusUpdateRequest reqBody
    ) {
        // 주문 상태 업데이트 로직 (예: 결제 완료, 배송 중 등)
        orderService.updateOrderStatus(orderId, reqBody.newStatus());

        // 업데이트된 주문 정보 반환
        Optional<Orders> updatedOrder = orderService.getOrderById(orderId);
        return ResponseEntity.ok(ApiResponse.success(new OrderCreateResponse(updatedOrder)));
    }

    @GetMapping
    @Operation(summary = "사용자 주문 목록 조회")
    public ResponseEntity<ApiResponse<List<OrderSummaryResponse>>> getOrders(
            @RequestParam Long userId
    ) {
        //주문 조회 로직
        List<OrderSummaryResponse> summaries = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(summaries));
    }
}
