package com.backend.domain.order.service;

import com.backend.domain.menu.entity.Menu;
import com.backend.domain.menu.repository.MenuRepository;
import com.backend.domain.order.controller.OrderController;
import com.backend.domain.order.entity.OrderDetails;
import com.backend.domain.order.entity.OrderStatus;
import com.backend.domain.order.entity.Orders;
import com.backend.domain.order.repository.OrderRepository;
import com.backend.domain.user.user.entity.Users;
import com.backend.domain.user.user.repository.UserRepository;
import com.backend.global.exception.BusinessException;
import com.backend.global.response.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository usersRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public Orders createOrder(OrderController.OrderCreateRequest request) {
        // 1. 유저 확인
        Users user = (Users) usersRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));

        // 2. 주문 항목 처리
        List<OrderDetails> orderDetails = new ArrayList<>();
        int calculatedTotal = 0;

        for (OrderController.OrderItemRequest itemReq : request.items()) {
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

        // 4. 주문 엔티티 생성
        Orders order = new Orders(user, calculatedTotal, OrderStatus.PAYMENT_PENDING);
        order.addOrderDetails(orderDetails);

        return orderRepository.save(order);
    }

    @Transactional
    public void updateOrderStatus(Long orderId, String status) {
        Optional<Orders> order = orderRepository.findById(orderId);
        // String → Enum 변환
        OrderStatus newStatus = OrderStatus.valueOf(status.toUpperCase());

        // 상태 업데이트
        order.get().setOrderStatus(newStatus);
    }

    public Optional<Orders> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
//                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.ORDER_NOT_FOUND.getMessage()));
    }

    public List<Orders> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserUserId(userId);
    }
}
