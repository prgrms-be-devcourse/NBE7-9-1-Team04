package com.backend.domain.cart.controller.dto.request;

import com.backend.domain.cart.entity.Cart;
import com.backend.domain.menu.entity.Menu;
import com.backend.domain.user.user.entity.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "장바구니 메뉴 추가 요청 DTO")
public class CartAddRequest {

    @Schema(description = "추가할 메뉴의 ID", example = "1")
    private Long menuId;

    @Schema(description = "추가할 메뉴의 수량", example = "2")
    private int quantity;

    public Cart toEntity(Users user, Menu menu) {
        return Cart.builder()
                .user(user)
                .menu(menu)
                .quantity(this.quantity)
                .build();
    }
}
