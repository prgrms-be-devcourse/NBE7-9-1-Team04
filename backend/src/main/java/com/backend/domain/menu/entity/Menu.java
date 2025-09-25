package com.backend.domain.menu.entity;

import com.backend.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menu")
@Getter
@NoArgsConstructor
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long menuId;  // 메뉴 ID

    @Column(nullable = false, length = 255)
    private String name;  // 메뉴 이름

    @Column(nullable = false)
    private int price;    // 메뉴 가격

    @Column(nullable = false)
    private Boolean isSoldOut = false;  // 품절 여부

    @Column(columnDefinition = "TEXT")
    private String description; // 메뉴 설명

    @Column(length = 255)
    private String imageUrl;    // 메뉴 이미지 URL

    // 생성자
    @Builder
    public Menu(String name, int price, Boolean isSoldOut, String description, String imageUrl) {
        this.name = name;
        this.price = price;
        this.isSoldOut = isSoldOut != null ? isSoldOut : false;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // 메뉴 정보 업데이트 메서드
    public void updateMenu(String name, int price,  Boolean isSoldOut, String description, String imageUrl) {
        this.name = name;
        this.price = price;
        this.isSoldOut = isSoldOut;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public void setIsSoldOut(boolean b) {
        this.isSoldOut = b;
    }
}
