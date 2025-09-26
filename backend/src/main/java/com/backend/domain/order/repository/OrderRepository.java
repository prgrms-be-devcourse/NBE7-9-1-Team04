package com.backend.domain.order.repository;

import com.backend.domain.order.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByUser_UserId(Long userId);

    // 특정 주문이 요청한 사용자의 소유인지 검증하며 주문 조회
    // (결제 권한 검증용: Payment → Orders → User 경로로 소유자 확인)
    Optional<Orders> findByOrderIdAndUser_UserId(Long orderId, Long userId);
}
