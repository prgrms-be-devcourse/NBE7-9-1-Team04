package com.backend.domain.menu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "menu")
@Getter
@NoArgsConstructor
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long menuId;  // 메뉴 ID

    @Column(nullable = false, length = 255)
    private String name;  // 메뉴 이름

    @Column(nullable = false)
    private int price;    // 메뉴 가격

    @Column(nullable = false)
    private Boolean isSoldOut;  // 품절 여부

    @Column(columnDefinition = "TEXT")
    private String description; // 메뉴 설명

    @Column(length = 255)
    private String imageUrl;    // 메뉴 이미지 URL

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;  // 생성 시간

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;  // 수정 시간

    public Menu(String name, int price, Boolean isSoldOut, String description, String imageUrl) {
        this.name = name;
        this.price = price;
        this.isSoldOut = isSoldOut;
        this.description = description;
        this.imageUrl = imageUrl;
    }
}
