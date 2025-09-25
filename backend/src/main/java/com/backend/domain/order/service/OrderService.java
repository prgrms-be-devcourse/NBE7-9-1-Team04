package com.backend.domain.order.service;

import com.backend.domain.menu.entity.Menu;
import com.backend.domain.menu.repository.MenuRepository;
import com.backend.domain.order.dto.request.OrderCreateRequest;
import com.backend.domain.order.dto.request.OrderDetailsCreateRequest;
import com.backend.domain.order.dto.response.OrderSummaryDetailResponse;
import com.backend.domain.order.dto.response.OrderSummaryResponse;
import com.backend.domain.order.entity.OrderDetails;
import com.backend.domain.order.entity.OrderStatus;
import com.backend.domain.order.entity.Orders;
import com.backend.domain.order.repository.OrderRepository;
import com.backend.domain.user.user.entity.Users;
import com.backend.domain.user.user.repository.UserRepository;
import com.backend.domain.user.user.service.UserService;
import com.backend.global.exception.BusinessException;
import com.backend.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository usersRepository;
    private final MenuRepository menuRepository;
    private final UserService usersService;

    @Transactional
    public Orders createOrder(Users actor, OrderCreateRequest request) throws Exception {
        // 1. 유저는 이미 Rq에서 검증
        Users user = actor;

        // 2. 주문 항목 처리
        List<OrderDetails> orderDetails = new ArrayList<>();
        int calculatedTotal = 0;

        assert request.items() != null;
        for (OrderDetailsCreateRequest itemReq : request.items()) {
            Menu menu = menuRepository.findById(itemReq.productId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));

            if (itemReq.quantity() <= 0) {
                throw new BusinessException(ErrorCode.INVALID_QUANTITY);
            }

            int expectedPrice = menu.getPrice() * itemReq.quantity();
            if (expectedPrice != itemReq.orderPrice()) {
                throw new BusinessException(ErrorCode.INVALID_ORDER_PRICE);
            }

            calculatedTotal += expectedPrice;

            OrderDetails detail = new OrderDetails(menu, itemReq.quantity(), itemReq.orderPrice(), itemReq.menuName());
            orderDetails.add(detail);
        }

        // 3. 금액 검증
        if (calculatedTotal != request.amount()) {
            throw new BusinessException(ErrorCode.INVALID_ORDER_AMOUNT);
        }

        // 4. 주문 엔티티 생성 (Users 엔티티 사용)
        Orders order = new Orders(user, calculatedTotal, OrderStatus.CREATED);
        order.addOrderDetails(orderDetails);

        return orderRepository.save(order);
    }

    @Transactional
    public void updateOrderStatus(Long orderId, String status) {
        // 1. 주문 존재 확인
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ORDER));

        // 2. Enum 변환 검증
        OrderStatus newStatus;
        try {
            newStatus = OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.INVALID_ORDER_STATUS);
        }

        // 3. 상태 전이 검증
        if (!order.getOrderStatus().canTransitionTo(newStatus)) {
            throw new BusinessException(ErrorCode.INVALID_STATUS_TRANSITION);
        }

        // 4. 시간 검증
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        LocalDateTime yesterday2pm = today.minusDays(1).atTime(14, 0);
        LocalDateTime today2pm = today.atTime(14, 0);

        LocalDateTime createdAt = order.getCreateDate();
        if (createdAt.isBefore(yesterday2pm) || createdAt.isAfter(today2pm)) {
            throw new BusinessException(ErrorCode.INVALID_ORDER_PROCESSING_TIME);
        }

        // 5. 상태 업데이트
        order.setOrderStatus(newStatus);
    }

    public Optional<Orders> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    // 사용자 ID로 주문 목록 조회
    @Transactional(readOnly = true)
    public List<OrderSummaryResponse> getOrdersByUserId(Long userId) {
        List<Orders> orders = orderRepository.findByUser_UserId(userId);

        return orders.stream()
                .map(order -> new OrderSummaryResponse(
                        order.getOrderId(),
                        order.getCreateDate(),
                        order.getOrderAmount(),
                        order.getOrderStatus().name(),
                        order.getOrderDetails().stream()
                                .map(detail -> new OrderSummaryDetailResponse(
                                        detail.getMenu().getName(),
                                        detail.getQuantity(),
                                        detail.getOrderPrice()
                                ))
                                .toList()
                ))
                .toList();
    }
}
