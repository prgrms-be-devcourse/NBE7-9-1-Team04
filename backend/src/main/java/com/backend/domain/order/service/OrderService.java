package com.backend.domain.order.service;

import com.backend.domain.cart.service.CartService;
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
import com.backend.domain.user.address.dto.AddressDto;
import com.backend.domain.user.address.entity.Address;
import com.backend.domain.user.address.repository.AddressRepository;
import com.backend.domain.user.user.dto.UserDto;
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
    private final AddressRepository addressRepository;
    private final CartService cartService;

    @Transactional
    public Orders createOrder(UserDto actor, OrderCreateRequest request) throws Exception {
        // 1. 사용자 존재 확인
        Users user = usersRepository.findById(actor.userId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));

        // 2 주소 가져오기(이미 등록된 주소 중 하나)
        Address address = addressRepository.findById(request.addressId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ADDRESS));

        // 3. 주문 항목 처리
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

        // 4. 금액 검증
        if (calculatedTotal != request.amount()) {
            throw new BusinessException(ErrorCode.INVALID_ORDER_AMOUNT);
        }

        // 5. 주문 엔티티 생성 (Users 엔티티 사용)
        Orders order = new Orders(user, calculatedTotal, OrderStatus.CREATED, address);
        order.addOrderDetails(orderDetails);

        // 6. 장바구니에서 해당 아이템들 삭제
        List<Long> orderedMenuIds = orderDetails.stream()
                .map(d -> d.getMenu().getMenuId())
                .toList();
        cartService.deleteOrderedItems(actor, orderedMenuIds);

        // 7. 주문 저장
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
        LocalDateTime createdAt = order.getCreateDate();
        LocalDateTime cancelDeadline = createdAt.toLocalDate().plusDays(1).atTime(14, 0);

        if (LocalDateTime.now().isAfter(cancelDeadline)) {
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

        // 1. 주문 목록 조회
        List<Orders> orders = orderRepository.findByUser_UserId(userId);

        if (orders.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ORDER);
        }

        return orders.stream()
                .map(order -> {
                    AddressDto addressDto = order.getAddress() != null
                            ? new AddressDto(order.getAddress())
                            : null;

                    return new OrderSummaryResponse(
                            order.getOrderId(),
                            order.getCreateDate(),
                            order.getOrderAmount(),
                            order.getOrderStatus().name(),
                            addressDto != null
                                    ? addressDto.address() + " " + addressDto.addressDetail()
                                    : null,
                            order.getPayment() != null
                                    ? order.getPayment().getPaymentId()
                                    : null,
                            order.getOrderDetails().stream()
                                    .map(detail -> new OrderSummaryDetailResponse(
                                            detail.getMenu().getName(),
                                            detail.getQuantity(),
                                            detail.getOrderPrice()
                                    ))
                                    .toList()
                    );
                })
                .toList();
    }

    @Transactional
    public Orders cancelOrder(UserDto actor, Long orderId) {

        // 1. 주문 존재 확인
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ORDER));

        // 2. 주문이 해당 유저의 것인지 확인
        if (!order.getUser().getUserId().equals(actor.userId())) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        // 3. 주문 상태가 CREATED 또는 PAID인지 확인
        if (order.getOrderStatus() != OrderStatus.CREATED &&
                order.getOrderStatus() != OrderStatus.PAID) {
            throw new BusinessException(ErrorCode.INVALID_STATUS_TRANSITION);
        }

        // 4. 시간 검증
        LocalDateTime createdAt = order.getCreateDate();
        LocalDateTime cancelDeadline = createdAt.toLocalDate().plusDays(1).atTime(14, 0);

        if (LocalDateTime.now().isAfter(cancelDeadline)) {
            throw new BusinessException(ErrorCode.INVALID_ORDER_PROCESSING_TIME);
        }

        // 5. 주문 상태를 CANCELED로 변경
        order.setOrderStatus(OrderStatus.CANCELED);

        return order;
    }

    @Transactional
    public void deleteOrder(UserDto actor, Long orderId) {
        // 1. 주문 존재 확인
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ORDER));

        // 2. 주문이 해당 유저의 것인지 확인
        if (!order.getUser().getUserId().equals(actor.userId())) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        // 3. 주문 상태가 CANCELED인지 확인
        if (order.getOrderStatus() != OrderStatus.CANCELED) {
            throw new BusinessException(ErrorCode.INVALID_STATUS_TRANSITION);
        }

        // 4. 주문 삭제
        orderRepository.delete(order);
    }

    public OrderSummaryResponse getOrderByUserId(Long actor, Long orderId) {
        // 1. 주문 존재 확인
        Orders order = orderRepository.findById(orderId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ORDER));

        // 2. 주문이 해당 유저의 것인지 확인
        if (!order.getUser().getUserId().equals(actor)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        // DTO 변환
        List<OrderSummaryDetailResponse> items = order.getOrderDetails().stream()
                .map(od -> new OrderSummaryDetailResponse(
                        od.getMenu().getName(),
                        od.getQuantity(),
                        od.getOrderPrice()
                ))
                .toList();

        return new OrderSummaryResponse(
                order.getOrderId(),
                order.getCreateDate(),
                order.getOrderAmount(),
                order.getOrderStatus().name(),
                order.getAddress() != null ? order.getAddress().getAddressDetail() : null,
                order.getPayment() != null ? order.getPayment().getPaymentId() : null,
                items
        );
    }
}
