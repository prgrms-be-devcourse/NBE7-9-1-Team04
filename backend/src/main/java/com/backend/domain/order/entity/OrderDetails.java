package com.backend.domain.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주문 상세 테이블 (OrderDetails)
 * 용도: 특정 주문에 어떤 상품이 몇 개씩 포함되었는지 상세 품목(영수증 품목 리스트)을 저장합니다.
 */
@Entity
@Getter
@Table(name = "order_details")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    // Order 엔티티와의 관계 (다대일)
    @ManyToOne(fetch = FetchType.LAZY)
    private Orders order;

    // Menu 엔티티와의 관계 (다대일) - 메뉴 도메인이 만들어지면 연결
    // @ManyToOne(fetch = FetchType.LAZY)
    // private Menu menu;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int orderPrice;

    @Column(length = 100, nullable = false)
    private String menuName;
}