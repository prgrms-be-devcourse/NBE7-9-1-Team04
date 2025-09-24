package com.backend.domain.cart.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "장바구니 전체 조회 응답 DTO")
public class CartListResponse {

    @Schema(description = "장바구니에 담긴 메뉴 목록")
    private List<CartResponse> cartItems;

    @Schema(description = "장바구니 전체 주문 총액", example = "54000")
    private int grandTotal;

    public CartListResponse(List<CartResponse> cartItems) {
        this.cartItems = cartItems;
        this.grandTotal = cartItems.stream()
                .mapToInt(CartResponse::getOrderAmount)
                .sum(); // 모든 항목의 orderAmount를 합산
    }
}