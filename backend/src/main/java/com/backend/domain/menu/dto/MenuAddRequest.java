package com.backend.domain.menu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record MenuAddRequest(
        @NotBlank(message = "메뉴 이름은 필수입니다.")
        String name,

        @Positive(message = "가격은 0보다 커야 합니다.")
        int price,

        String description,
        String imageUrl
) {}
