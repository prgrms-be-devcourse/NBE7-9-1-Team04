package com.backend.domain.cart.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "장바구니 메뉴 추가 요청 DTO")
public class CartAddRequest {

    @Schema(description = "추가할 메뉴의 ID", example = "1")
    private Long menuId;

    @Schema(description = "추가할 메뉴의 수량", example = "2")
    private int quantity;
}
