package com.backend.domain.menu.dto;

import com.backend.domain.menu.entity.Menu;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record MenuAddRequest(
        @NotBlank(message = "메뉴 이름은 필수입니다.")
        @Schema(description = "메뉴 이름", example = "콜롬비아 수프리모")
        String name,

        @Schema(description = "가격", example = "25000")
        @Positive(message = "가격은 0보다 커야 합니다.")
        int price,

        @Schema(description = "품절 여부", example = "false")
        Boolean isSoldOut,

        @Schema(description = "설명", example = "균형잡힌 맛과 부드러운 바디감")
        String description,

        @Pattern(
                regexp = "^(https?://).+",
                message = "이미지 URL은 http:// 또는 https://로 시작해야 합니다."
        )
        String imageUrl
) {
    // DTO를 메뉴 엔티티로 변환하는 메서드
    public Menu toEntity() {
        return Menu.builder()
                .name(this.name)
                .price(this.price)
                .isSoldOut(this.isSoldOut)
                .description(this.description)
                .imageUrl(this.imageUrl)
                .build();
    }
}
