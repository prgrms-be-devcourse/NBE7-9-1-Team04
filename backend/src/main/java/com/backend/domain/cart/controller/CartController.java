package com.backend.domain.cart.controller;

import com.backend.domain.cart.controller.dto.request.CartAddRequest;
import com.backend.domain.cart.controller.dto.request.CartUpdateRequest;
import com.backend.domain.cart.controller.dto.response.CartListResponse;
import com.backend.domain.cart.controller.dto.response.CartResponse;
import com.backend.domain.cart.service.CartService;
import com.backend.domain.user.user.dto.UserDto;
import com.backend.global.response.ApiResponse;
import com.backend.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController implements CartSpecification {

    private final CartService cartService;
    private final Rq rq;

    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartResponse>> addCartItem(@RequestBody CartAddRequest request) throws Exception {
        UserDto currentUser = rq.getUser();
        CartResponse response = cartService.addCartItem(currentUser, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/items/{menuId}")
    public ResponseEntity<ApiResponse<CartResponse>> updateCartItemQuantity(
            @PathVariable Long menuId,
            @RequestBody CartUpdateRequest request) throws Exception {
        UserDto currentUser = rq.getUser();
        CartResponse response = cartService.updateCartItemQuantity(currentUser, menuId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/items/{menuId}")
    public ResponseEntity<ApiResponse<Void>> deleteCartItem(@PathVariable Long menuId) throws Exception {
        UserDto currentUser = rq.getUser();
        cartService.deleteCartItem(currentUser, menuId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CartListResponse>> getCart() throws Exception {
        UserDto currentUser = rq.getUser();
        CartListResponse response = cartService.getCart(currentUser);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> clearCart() throws Exception {
        UserDto currentUser = rq.getUser();
        cartService.clearCart(currentUser);
        return ResponseEntity.ok(ApiResponse.success());
    }
}