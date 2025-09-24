package com.backend.domain.order.entity;

import com.backend.domain.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 주문 상세 테이블 (OrderDetails)
 * 용도: 특정 주문에 어떤 상품이 몇 개씩 포함되었는지 상세 품목(영수증 품목 리스트)을 저장합니다.
 */
@Entity
@Getter
@Setter
@Table(name = "order_details")
@NoArgsConstructor
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    // Order 엔티티와의 관계 (다대일)
    @ManyToOne(fetch = FetchType.LAZY)
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "menu_menu_id")
    private Menu menu;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int orderPrice;

    @Column(length = 100, nullable = false)
    private String menuName;
}