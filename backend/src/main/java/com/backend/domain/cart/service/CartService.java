package com.backend.domain.cart.service;

import com.backend.domain.cart.controller.dto.request.CartAddRequest;
import com.backend.domain.cart.controller.dto.response.CartResponse;
import com.backend.domain.cart.entity.Cart;
import com.backend.domain.cart.repository.CartRepository;
import com.backend.domain.menu.entity.Menu;
import com.backend.domain.menu.repository.MenuRepository;
import com.backend.domain.user.user.entity.User;
import com.backend.domain.user.user.repository.UserRepository;
import com.backend.global.exception.BusinessException;
import com.backend.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;

    private static final Long TEST_USER_ID = 1L; // 임시 사용자

    @Transactional
    public CartResponse addCartItem(CartAddRequest request) {

        User testUser = userRepository.findById(TEST_USER_ID)
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
}
