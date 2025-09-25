package com.backend.domain.order.controller;

import com.backend.domain.order.dto.response.AdminOrderSummaryResponse;
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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@Tag(name = "AdminOrderController", description = "관리자 주문 API")
public class AdminOrderController {

    private final AdminOrderService adminOrderService;
    private final Rq rq;

    @GetMapping
    @Operation(summary = "관리자 주문 목록 조회")
    public ResponseEntity<ApiResponse<List<AdminOrderSummaryResponse>>> getOrders() throws Exception {
        //쿠키에서 인증된 유저 가져오기
        UserDto actor = rq.getUser();

        // 전체 주문 조회
        List<AdminOrderSummaryResponse> summaries = adminOrderService.getAllOrdersForAdmin();
        return ResponseEntity.ok(ApiResponse.success(summaries));
    }
}
