package com.backend.domain.cart.service;

import com.backend.domain.cart.controller.dto.request.CartAddRequest;
import com.backend.domain.cart.controller.dto.request.CartUpdateRequest;
import com.backend.domain.cart.controller.dto.response.CartListResponse;
import com.backend.domain.cart.controller.dto.response.CartResponse;
import com.backend.domain.cart.entity.Cart;
import com.backend.domain.cart.repository.CartRepository;
import com.backend.domain.menu.entity.Menu;
import com.backend.domain.menu.repository.MenuRepository;
import com.backend.domain.user.user.entity.Users;
import com.backend.domain.user.user.repository.UserRepository;
import com.backend.global.exception.BusinessException;
import com.backend.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;

    private static final Long TEST_USER_ID = 1L; // 임시 사용자

    @Transactional
    public CartResponse addCartItem(CartAddRequest request) {

        Users testUser = userRepository.findById(TEST_USER_ID)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));

        Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));

        Cart cartItem = cartRepository.findByUserAndMenu(testUser, menu)
                .orElse(null);

        if (cartItem != null) {
            cartItem.updateQuantity(cartItem.getQuantity() + request.getQuantity());
        } else {
            cartItem = request.toEntity(testUser, menu);
            cartRepository.save(cartItem);
        }
        return CartResponse.from(cartItem);
    }

    @Transactional
    public CartResponse updateCartItemQuantity(Long menuId, CartUpdateRequest request) {
        Users testUser = userRepository.findById(TEST_USER_ID)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));

        if (request.getQuantity() <= 0) {
            throw new BusinessException(ErrorCode.INVALID_QUANTITY);
        }

        Cart cartItem = cartRepository.findByUserAndMenu(testUser, menu)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));

        cartItem.updateQuantity(request.getQuantity());

        return CartResponse.from(cartItem);

    }

    @Transactional
    public void deleteCartItem(Long menuId) {
        Users testUser = userRepository.findById(TEST_USER_ID)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));

        Cart cartItem = cartRepository.findByUserAndMenu(testUser, menu)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));

        cartRepository.delete(cartItem);
    }

    @Transactional(readOnly = true)
    public CartListResponse getCart() {
        Users user = userRepository.findById(TEST_USER_ID)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));

        List<Cart> cartItems = cartRepository.findByUser(user);

        List<CartResponse> cartResponses = cartItems.stream()
                .map(CartResponse::from)
                .collect(Collectors.toList());

        return new CartListResponse(cartResponses);
    }
}
