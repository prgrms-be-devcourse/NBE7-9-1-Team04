package com.backend.domain.cart.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "장바구니 개별 항목 응답 DTO")
public class CartResponse {

    @Schema(description = "장바구니 항목 ID", example = "101")
    private Long cartId;

    @Schema(description = "메뉴 ID", example = "1")
    private Long menuId;

    @Schema(description = "메뉴 이름", example = "원두")
    private String name;

    @Schema(description = "메뉴 이미지 URL", example = "http://example.com/images/coffee.jpg")
    private String imageUrl;

    @Schema(description = "메뉴 개당 가격", example = "18000")
    private int price;

    @Schema(description = "담은 수량", example = "2")
    private int quantity;

    @Schema(description = "항목별 총 금액 (가격 * 수량)", example = "36000")
    private int orderAmount;

    @Builder
    public CartResponse(Long cartId, Long menuId, String name, String imageUrl, int price, int quantity) {
        this.cartId = cartId;
        this.menuId = menuId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantity = quantity;
        this.orderAmount = price * quantity; // 항목별 총액 계산
    }
}
