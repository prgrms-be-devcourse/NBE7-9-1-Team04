package com.backend.domain.order.controller;

import com.backend.domain.order.dto.request.OrderCreateRequest;
import com.backend.domain.order.dto.request.OrderStatusUpdateRequest;
import com.backend.domain.order.dto.response.OrderCancelResponse;
import com.backend.domain.order.dto.response.OrderCreateResponse;
import com.backend.domain.order.dto.response.OrderDeleteResponse;
import com.backend.domain.order.dto.response.OrderSummaryResponse;
import com.backend.domain.order.entity.Orders;
import com.backend.domain.order.service.OrderService;
import com.backend.domain.user.user.dto.UserDto;
import com.backend.global.response.ApiResponse;
import com.backend.global.rq.Rq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "OrderController", description = "주문 API")
public class OrderController {

    private final OrderService orderService;
    private final Rq rq;

    @PostMapping
    @Transactional
    @Operation(summary = "주문 생성", description = "인증된 사용자의 주문을 생성합니다. CREATED 상태로 시작합니다.")
    public ResponseEntity<ApiResponse<OrderCreateResponse>> createOrder(
            @Valid @RequestBody OrderCreateRequest request
    ) throws Exception {

        UserDto actor = rq.getUser();
        Orders order = orderService.createOrder(actor, request);

        return ResponseEntity.ok(ApiResponse.success(new OrderCreateResponse((order))));
    }

    @PutMapping("/{orderId}/status")
    @Transactional
    @Operation(summary = "주문 상태 업데이트", description = "주문의 상태를 업데이트합니다. (예: PAID, COMPLETED 등)")
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
    @Operation(summary = "사용자 주문 목록 조회", description = "인증된 사용자의 모든 주문 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<OrderSummaryResponse>>> getOrders() throws Exception {
        //쿠키에서 인증된 유저 가져오기
        UserDto actor = rq.getUser();

        //주문 조회 로직
        List<OrderSummaryResponse> summaries = orderService.getOrdersByUserId(actor.userId());
        return ResponseEntity.ok(ApiResponse.success(summaries));
    }

    @PutMapping("/{orderId}/cancel")
    @Operation(summary = "주문 취소", description = "주문을 취소합니다. 취소 가능한 상태인지 확인 후 처리합니다.")
    public ResponseEntity<ApiResponse<OrderCancelResponse>> cancelOrder(
            @PathVariable Long orderId
    ) throws Exception {
        UserDto actor = rq.getUser();
        Orders order = orderService.cancelOrder(actor, orderId);

        // 엔티티 -> DTO 변환
        OrderCancelResponse response = new OrderCancelResponse(
                order.getOrderId(),
                "주문이 성공적으로 취소되었습니다.",
                order.getOrderStatus().name()
        );

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{orderId}/delete")
    @Operation(summary = "주문 삭제", description = "주문을 삭제합니다. 삭제 가능한 상태인지 확인 후 처리합니다.")
    public ResponseEntity<ApiResponse<OrderDeleteResponse>> deleteOrder(
            @PathVariable Long orderId
    ) throws Exception {
        UserDto actor = rq.getUser();
        orderService.deleteOrder(actor, orderId);

        // 엔티티 -> DTO 변환
        OrderDeleteResponse response = new OrderDeleteResponse(
                orderId,
                "주문이 성공적으로 삭제되었습니다."
        );

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
