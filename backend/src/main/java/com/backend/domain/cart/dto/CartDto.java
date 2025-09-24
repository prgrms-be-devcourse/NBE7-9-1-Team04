package com.backend.domain.cart.dto;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class CartDto {

    private Long cartId;
    private Long userId;
    private Long menuId;
    private int quantity;
    private int orderAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
