package com.backend.domain.menu.dto;

import com.backend.domain.menu.entity.Menu;

public record MenuResponse(
        Long menuId,
        String name,
        int price,
        Boolean isSoldOut,
        String description,
        String imageUrl
) {
    public static MenuResponse from(Menu menu) {
        return new MenuResponse(
                menu.getMenuId(),
                menu.getName(),
                menu.getPrice(),
                menu.getIsSoldOut(),
                menu.getDescription(),
                menu.getImageUrl()
        );
    }
}
