package com.backend.domain.order.controller;

import com.backend.domain.order.dto.request.OrderStatusUpdateRequest;
import com.backend.domain.order.dto.response.AdminOrderSummaryResponse;
import com.backend.domain.order.dto.response.OrderCreateResponse;
import com.backend.domain.order.entity.Orders;
import com.backend.domain.order.service.AdminOrderService;
import com.backend.domain.order.service.OrderService;
import com.backend.domain.user.user.dto.UserDto;
import com.backend.domain.user.user.entity.Users;
import com.backend.global.exception.BusinessException;
import com.backend.global.response.ApiResponse;
import com.backend.global.response.ErrorCode;
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
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@Tag(name = "AdminOrderController", description = "관리자 주문 API")
public class AdminOrderController {

    private final AdminOrderService adminOrderService;
    private final OrderService orderService;
    private final Rq rq;

    @GetMapping
    @Operation(summary = "관리자 주문 목록 조회", description = "모든 주문 목록을 조회합니다. (관리자 전용)")
    public ResponseEntity<ApiResponse<List<AdminOrderSummaryResponse>>> getOrders() throws Exception {
        //쿠키에서 인증된 유저 가져오기
        UserDto actor = rq.getUser();

        //관리자 인증 로직 구현 예정
        if (actor.level() != 1) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ADMIN);
        }

        // 전체 주문 조회
        List<AdminOrderSummaryResponse> summaries = adminOrderService.getAllOrdersForAdmin();
        return ResponseEntity.ok(ApiResponse.success(summaries));
    }
}
