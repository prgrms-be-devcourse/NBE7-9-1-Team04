package com.backend.domain.cart.service;

import com.backend.domain.cart.controller.dto.request.CartAddRequest;
import com.backend.domain.cart.controller.dto.request.CartUpdateRequest;
import com.backend.domain.cart.controller.dto.response.CartListResponse;
import com.backend.domain.cart.controller.dto.response.CartResponse;
import com.backend.domain.cart.entity.Cart;
import com.backend.domain.cart.repository.CartRepository;
import com.backend.domain.menu.entity.Menu;
import com.backend.domain.menu.repository.MenuRepository;
import com.backend.domain.user.user.dto.UserDto;
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

    /**
     * UserDto를 받아 Users 엔티티를 조회하는 헬퍼 메서드
     * 필요성 : 가독성 ⬆️, 중복 제거 ⭕️, 각 메서드의 책임 명확히 분리
     * 이미 검증된 유효한 데이터이므로 get() 사용
    */
    private Users getUserEntity(UserDto userDto) {
        return userRepository.findById(userDto.userId()).get();
    }

    @Transactional
    public CartResponse addCartItem(UserDto userDto, CartAddRequest request) {

        Users user = getUserEntity(userDto);

        Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));

        Cart cartItem = cartRepository.findByUserAndMenu(user, menu)
                .orElse(null);

        if (cartItem != null) {
            cartItem.updateQuantity(cartItem.getQuantity() + request.getQuantity());
        } else {
            cartItem = request.toEntity(user, menu);
            cartRepository.save(cartItem);
        }
        return CartResponse.from(cartItem);
    }

    @Transactional
    public CartResponse updateCartItemQuantity(UserDto userDto, Long menuId, CartUpdateRequest request) {
        Users user = getUserEntity(userDto);

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));

        if (request.getQuantity() <= 0) {
            throw new BusinessException(ErrorCode.INVALID_QUANTITY);
        }

        Cart cartItem = cartRepository.findByUserAndMenu(user, menu)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));

        cartItem.updateQuantity(request.getQuantity());

        return CartResponse.from(cartItem);

    }

    @Transactional
    public void deleteCartItem(UserDto userDto, Long menuId) {

        Users user = getUserEntity(userDto);

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));

        Cart cartItem = cartRepository.findByUserAndMenu(user, menu)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));

        cartRepository.delete(cartItem);
    }

    @Transactional(readOnly = true)
    public CartListResponse getCart(UserDto userDto) {

        Users user = getUserEntity(userDto);

        List<Cart> cartItems = cartRepository.findByUser(user);

        List<CartResponse> cartResponses = cartItems.stream()
                .map(CartResponse::from)
                .collect(Collectors.toList());

        return new CartListResponse(cartResponses);
    }

    @Transactional
    public void clearCart(UserDto userDto) {

        Users user = getUserEntity(userDto);

        cartRepository.deleteAllByUser(user);
    }
}
