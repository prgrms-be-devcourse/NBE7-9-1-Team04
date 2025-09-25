package com.backend.domain.cart.controller;

import com.backend.domain.cart.controller.dto.request.CartAddRequest;
import com.backend.domain.cart.controller.dto.request.CartUpdateRequest;
import com.backend.domain.cart.controller.dto.response.CartListResponse;
import com.backend.domain.cart.controller.dto.response.CartResponse;
import com.backend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Cart", description = "장바구니 API")
public interface CartSpecification {

    @Operation(summary = "장바구니 메뉴 추가", description = "장바구니에 새로운 메뉴를 추가합니다.")
    ResponseEntity<ApiResponse<CartResponse>> addCartItem(@RequestBody CartAddRequest request) throws Exception;

    @Operation(summary = "장바구니 메뉴 수량 변경", description = "특정 메뉴의 수량을 변경합니다.")
    ResponseEntity<ApiResponse<CartResponse>> updateCartItemQuantity (@PathVariable Long menuId, @RequestBody CartUpdateRequest request) throws Exception;

    @Operation(summary = "장바구니 메뉴 삭제", description = "장바구니에서 특정 메뉴를 삭제합니다.")
    ResponseEntity<ApiResponse<Void>> deleteCartItem(@PathVariable Long menuId) throws Exception;

    @Operation(summary = "장바구니 전체 조회", description = "현재 사용자의 장바구니 전체 목록을 조회합니다.")
    ResponseEntity<ApiResponse<CartListResponse>> getCart() throws Exception;

    @Operation(summary = "장바구니 비우기", description = "장바구니의 모든 메뉴를 삭제합니다.")
    ResponseEntity<ApiResponse<Void>> clearCart() throws Exception;
}
