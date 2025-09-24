package com.backend.domain.menu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record MenuAddRequest(
        @NotBlank(message = "메뉴 이름은 필수입니다.")
        @Schema(description = "메뉴 이름", example = "아메리카노")
        String name,

        @Schema(description = "가격", example = "25000")
        @Positive(message = "가격은 0보다 커야 합니다.")
        int price,

        @Schema(description = "설명", example = "진한 에스프레소와 물")
        String description,


        @Pattern(
                regexp = "^(https?://).+",
                message = "이미지 URL은 http:// 또는 https://로 시작해야 합니다."
        )
        String imageUrl
) {}
