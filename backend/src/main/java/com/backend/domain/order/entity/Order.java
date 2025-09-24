package com.backend.domain.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 주문 테이블 (Order)
 * 용도: 고객의 주문 한 건에 대한 전체적인 요약 정보(영수증 머리글)를 저장합니다.
 */
@Entity
@Getter
@Table(name = "order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private int orderAmount;

    @Column(nullable = false)
    private LocalDateTime orderTime;

    // OrderDetails 엔티티와의 관계 (일대다)
    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderDetails> orderDetails = new ArrayList<>();

//    // User 엔티티와의 관계 (다대일)
//    @ManyToOne(fetch = FetchType.LAZY)
//    private User user;

    // Address 엔티티와의 관계 (다대일) - 주소 도메인이 만들어지면 연결
    // @ManyToOne(fetch = FetchType.LAZY)
    // private Address address;

    // Payment 엔티티와의 관계 (일대일) - 결제 도메인이 만들어지면 연결
    // @OneToOne(fetch = FetchType.LAZY)
    // private Payment payment;
}