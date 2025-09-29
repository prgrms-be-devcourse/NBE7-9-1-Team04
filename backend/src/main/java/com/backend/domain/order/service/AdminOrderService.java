package com.backend.domain.order.service;

import com.backend.domain.order.dto.response.AdminOrderSummaryResponse;
import com.backend.domain.order.dto.response.OrderSummaryDetailResponse;
import com.backend.domain.order.entity.Orders;
import com.backend.domain.order.repository.OrderRepository;
import com.backend.domain.user.address.dto.AddressDto;
import com.backend.domain.user.address.service.AddressService;
import com.backend.domain.user.user.dto.UserDto;
import com.backend.global.exception.BusinessException;
import com.backend.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminOrderService {

    private final OrderRepository orderRepository;
    private final AddressService addressService;

    @Transactional
    public List<AdminOrderSummaryResponse> getAllOrdersForAdmin() {
        List<Orders> orders = orderRepository.findAll();

        // 주문이 없을 경우 → 그냥 빈 리스트 반환
        if (orders.isEmpty()) {
            return List.of();
        }

        return orders.stream()
                .map(order -> {
                    // AddressDto 변환
                    AddressDto addressDto = order.getAddress() != null
                            ? new AddressDto(order.getAddress())
                            : null;

                    return new AdminOrderSummaryResponse(
                            order.getOrderId(),
                            order.getCreateDate(),
                            order.getOrderAmount(),
                            order.getOrderStatus().name(),
                            order.getUser().getEmail(),
                            order.getUser().getPhoneNumber(),
                            addressDto != null ? addressDto.address() + " " + addressDto.addressDetail() : null,
                            order.getPayment() != null ? order.getPayment().getPaymentId() : null,
                            order.getOrderDetails().stream()
                                    .map(detail -> new OrderSummaryDetailResponse(
                                            detail.getMenu().getName(),
                                            detail.getQuantity(),
                                            detail.getOrderPrice(),
                                            detail.getMenu() != null ? detail.getMenu().getImageUrl() : null
                                    ))
                                    .toList()
                    );
                })
                .toList();
    }
}
