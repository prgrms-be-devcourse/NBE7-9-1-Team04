package com.backend.domain.menu.dto;

import com.backend.domain.menu.entity.Menu;

public record MenuDto(
        Long menuId,
        String name,
        int price,
        Boolean isSoldOut,
        String description,
        String imageUrl
) {
    public MenuDto(Menu menu) {
        this(
                menu.getMenuId(),
                menu.getName(),
                menu.getPrice(),
                menu.getIsSoldOut(),
                menu.getDescription(),
                menu.getImageUrl()
        );
    }

}
