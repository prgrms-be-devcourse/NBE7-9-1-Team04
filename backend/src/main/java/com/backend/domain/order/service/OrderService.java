package com.backend.domain.order.service;

import com.backend.domain.order.controller.OrderController;
import com.backend.domain.order.entity.OrderDetails;
import com.backend.domain.order.entity.OrderStatus;
import com.backend.domain.order.entity.Orders;
import com.backend.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Transactional
    public Orders createOrder(OrderController.OrderCreateRequest request) {
        // 1. 주문 엔티티 생성
        Orders order = new Orders();
        order.setOrderStatus(OrderStatus.PAYMENT_PENDING);

        //프론트에서 전달받은 amount 사용
        order.setOrderAmount(request.amount());

        // 2. 주문 상세 엔티티 생성 및 매핑
        request.items().forEach(item -> {
            OrderDetails details = new OrderDetails();
            details.setMenuName(item.menuName());
            details.setQuantity(item.quantity());
            details.setOrderPrice(item.orderPrice());
            // productId는 Menu 도메인 완성되면 매핑

            order.addOrderDetail(details);
        });

        // 3. 저장
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
