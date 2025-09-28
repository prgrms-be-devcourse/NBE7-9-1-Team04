package com.backend.grobal.scheduler;

import com.backend.domain.order.entity.OrderStatus;
import com.backend.domain.order.entity.Orders;
import com.backend.domain.order.repository.OrderRepository;
import com.backend.domain.user.address.dto.AddressDto;
import com.backend.domain.user.address.entity.Address;
import com.backend.domain.user.address.repository.AddressRepository;
import com.backend.domain.user.user.entity.Users;
import com.backend.domain.user.user.repository.UserRepository;
import com.backend.global.scheduler.OrderScheduler;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class OrderSchedulerTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private EntityManager em;

    // 👉 스케줄러 주입
    @Autowired
    private OrderScheduler orderScheduler;

    private Users user;
    private Address address;
    private LocalDateTime today2pm;
    private LocalDateTime yesterday2pm;

    // 매 테스트마다 초기화
    @BeforeEach
    void setup() {
        orderRepository.deleteAll();
        user = userRepository.save(new Users("user@test.com", "pw", "010-1111-2222", 1));
        address = addressRepository.save(new Address(user, new AddressDto(null, null, "Seoul", "101", "12345")));

        LocalDateTime now = LocalDateTime.now();
        today2pm = now.toLocalDate().atTime(14, 0);
        yesterday2pm = today2pm.minusDays(1);
    }

    // 테스트 편의를 위한 주문 생성일 강제 변경 메서드
    private void forceCreateDate(Orders order, LocalDateTime newDate) {
        em.createQuery("UPDATE Orders o SET o.createDate = :date WHERE o.orderId = :id")
                .setParameter("date", newDate)
                .setParameter("id", order.getOrderId())
                .executeUpdate();
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("PAID 상태의 주문이 범위 내에 있을 때 -> 상태가 COMPLETED로 변경됨")
    void test1() {
        Orders order = new Orders(user, 10000, OrderStatus.PAID, address);
        orderRepository.saveAndFlush(order);
        forceCreateDate(order, yesterday2pm.plusHours(1)); // in range

        // when: 스케줄러 실행
        orderScheduler.completeDailyOrders();

        // then
        Orders updated = orderRepository.findById(order.getOrderId()).orElseThrow();
        assertEquals(OrderStatus.COMPLETED, updated.getOrderStatus());
    }

    @Test
    @DisplayName("CREATED 상태의 주문이 범위 내에 있을 때 -> 상태가 변경되지 않음")
    void test2() {
        Orders order = new Orders(user, 20000, OrderStatus.CREATED, address);
        orderRepository.saveAndFlush(order);
        forceCreateDate(order, yesterday2pm.plusHours(2)); // in range

        // when: 스케줄러 실행
        orderScheduler.completeDailyOrders();

        // then
        Orders updated = orderRepository.findById(order.getOrderId()).orElseThrow();
        assertEquals(OrderStatus.CREATED, updated.getOrderStatus());
    }

    @Test
    @DisplayName("PAID 상태의 주문이 범위 밖에 있을 때 -> 상태가 변경되지 않음")
    void test3() {
        Orders order = new Orders(user, 30000, OrderStatus.PAID, address);
        orderRepository.saveAndFlush(order);
        forceCreateDate(order, yesterday2pm.minusHours(5)); // out of range

        // when: 스케줄러 실행
        orderScheduler.completeDailyOrders();

        // then
        Orders updated = orderRepository.findById(order.getOrderId()).orElseThrow();
        assertEquals(OrderStatus.PAID, updated.getOrderStatus());
    }

    @Test
    @DisplayName("CANCELED 상태의 주문이 범위 내에 있을 때 -> 상태가 변경되지 않음")
    void test4() {
        Orders order = new Orders(user, 40000, OrderStatus.CANCELED, address);
        orderRepository.saveAndFlush(order);
        forceCreateDate(order, yesterday2pm.plusHours(3)); // in range

        // when: 스케줄러 실행
        orderScheduler.completeDailyOrders();

        // then
        Orders updated = orderRepository.findById(order.getOrderId()).orElseThrow();
        assertEquals(OrderStatus.CANCELED, updated.getOrderStatus());
    }

    @Test
    @DisplayName("COMPLETED 상태의 주문이 범위 내에 있을 때 -> 상태가 변경되지 않음")
    void test5() {
        Orders order = new Orders(user, 50000, OrderStatus.COMPLETED, address);
        orderRepository.saveAndFlush(order);
        forceCreateDate(order, yesterday2pm.plusHours(4)); // in range

        // when: 스케줄러 실행
        orderScheduler.completeDailyOrders();

        // then
        Orders updated = orderRepository.findById(order.getOrderId()).orElseThrow();
        assertEquals(OrderStatus.COMPLETED, updated.getOrderStatus());
    }

}
