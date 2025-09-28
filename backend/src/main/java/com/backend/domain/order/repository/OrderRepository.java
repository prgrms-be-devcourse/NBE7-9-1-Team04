package com.backend.domain.order.repository;

import com.backend.domain.order.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByUser_UserId(Long userId);

    // 특정 주문이 요청한 사용자의 소유인지 검증하며 주문 조회
    // (결제 권한 검증용: Payment → Orders → User 경로로 소유자 확인)
    Optional<Orders> findByOrderIdAndUser_UserId(Long orderId, Long userId);

    // 특정 기간 내에 'PAID' 상태인 주문들을 'COMPLETED' 상태로 일괄 업데이트
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE Orders o
           SET o.orderStatus = com.backend.domain.order.entity.OrderStatus.COMPLETED
         WHERE o.createDate >= :start
           AND o.createDate <  :end
           AND o.orderStatus = com.backend.domain.order.entity.OrderStatus.PAID
    """)
    int bulkCompleteBetween(@Param("start") LocalDateTime start,
                            @Param("end") LocalDateTime end);
}
