package com.backend.domain.cart.entity;

import com.backend.domain.menu.entity.Menu;
import com.backend.domain.user.user.entity.User;
import com.backend.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menuId", nullable = false)
    private Menu menu;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "orderAmount", nullable = false)
    private Integer orderAmount;

    @Builder
    public Cart(User user, Menu menu, Integer quantity) {
        this.user = user;
        this.menu = menu;
        this.quantity = quantity;
        this.orderAmount = menu.getPrice() * quantity;
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
        this.orderAmount = this.menu.getPrice() * quantity;
    }
}