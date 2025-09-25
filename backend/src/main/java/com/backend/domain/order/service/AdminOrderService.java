package com.backend.domain.order.service;

import com.backend.domain.menu.repository.MenuRepository;
import com.backend.domain.order.dto.response.AdminOrderSummaryResponse;
import com.backend.domain.order.dto.response.OrderSummaryDetailResponse;
import com.backend.domain.order.entity.Orders;
import com.backend.domain.order.repository.OrderRepository;
import com.backend.domain.user.user.repository.UserRepository;
import com.backend.domain.user.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminOrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public List<AdminOrderSummaryResponse> getAllOrdersForAdmin() {
        List<Orders> orders = orderRepository.findAll();

        return orders.stream()
                .map(order -> new AdminOrderSummaryResponse(
                        order.getOrderId(),
                        order.getCreateDate(),
                        order.getOrderAmount(),
                        order.getOrderStatus().name(),
                        order.getUser().getEmail(),
                        order.getUser().getPhoneNumber(),
                        order.getAddress() != null ? order.getAddress().toString() : null,
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
