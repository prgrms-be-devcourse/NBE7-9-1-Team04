package com.backend.domain.cart.controller;

import com.backend.domain.cart.controller.dto.request.CartAddRequest;
import com.backend.domain.cart.controller.dto.request.CartUpdateRequest;
import com.backend.domain.cart.controller.dto.response.CartListResponse;
import com.backend.domain.cart.controller.dto.response.CartResponse;
import com.backend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController implements CartSpecification {

    @Override
    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartResponse>> addCartItem(@RequestBody CartAddRequest request) {
        // TODO: 장바구니 추가 로직 구현
        CartResponse response = CartResponse.builder().build(); // 임시 응답 객체
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Override
    @PutMapping("/items/{menuId}")
    public ResponseEntity<ApiResponse<CartResponse>> updateCartItemQuantity(
            @PathVariable Long menuId,
            @RequestBody CartUpdateRequest request) {
        // TODO: 장바구니 수량 변경 로직 구현
        CartResponse response = CartResponse.builder().build();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Override
    @DeleteMapping("/items/{menuId}")
    public ResponseEntity<ApiResponse<Void>> deleteCartItem(@PathVariable Long menuId) {
        // TODO: 장바구니 특정 항목 삭제 로직 구현
        return ResponseEntity.ok(ApiResponse.success());
    }

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<CartListResponse>> getCart() {
        // TODO: 장바구니 전체 조회 로직 구현
        CartListResponse response = new CartListResponse(new ArrayList<>());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Override
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> clearCart() {
        // TODO: 장바구니 전체 비우기 로직 구현
        return ResponseEntity.ok(ApiResponse.success());
    }
}