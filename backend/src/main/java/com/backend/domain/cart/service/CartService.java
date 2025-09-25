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

    @Transactional
    public CartResponse addCartItem(UserDto userDto, CartAddRequest request) {

        Users user = userRepository.findById(userDto.userId()).get();

        Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));

        Cart cartItem = cartRepository.findByUser_UserIdAndMenu_MenuId(userDto.userId(), menu.getMenuId())
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
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));

        if (request.getQuantity() <= 0) {
            throw new BusinessException(ErrorCode.INVALID_QUANTITY);
        }

        Cart cartItem = cartRepository.findByUser_UserIdAndMenu_MenuId(userDto.userId(), menuId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));

        cartItem.updateQuantity(request.getQuantity());

        return CartResponse.from(cartItem);
    }

    @Transactional
    public void deleteCartItem(UserDto userDto, Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));

        Cart cartItem = cartRepository.findByUser_UserIdAndMenu_MenuId(userDto.userId(), menuId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));

        cartRepository.delete(cartItem);
    }

    @Transactional(readOnly = true)
    public CartListResponse getCart(UserDto userDto) {

        List<Cart> cartItems = cartRepository.findByUser_UserId(userDto.userId());

        List<CartResponse> cartResponses = cartItems.stream()
                .map(CartResponse::from)
                .collect(Collectors.toList());

        return new CartListResponse(cartResponses);
    }

    @Transactional
    public void clearCart(UserDto userDto) {
        cartRepository.deleteAllByUser_UserId(userDto.userId());
    }

    /**
     * 주문 완료 시 장바구니에서 해당 메뉴 삭제
     * parameter : 주문 완료된 munuId 리스트
     */
    @Transactional
    public void deleteOrderedItems(UserDto userDto, List<Long> orderedMenuIds) {
        List<Cart> cartItemsToDelete = cartRepository.findByUser_UserIdAndMenu_MenuIdIn(userDto.userId(), orderedMenuIds);
        cartRepository.deleteAll(cartItemsToDelete);
    }
}
