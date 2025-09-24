package com.backend.domain.cart.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "장바구니 메뉴 수량 변경 요청 DTO")
public class CartUpdateRequest {

    @Schema(description = "변경할 수량", example = "3")
    private int quantity;
}
