package com.backend.domain.menu.entity;

import com.backend.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
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

    public Menu(String name, int price, String description, String imageUrl) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
    }
}
