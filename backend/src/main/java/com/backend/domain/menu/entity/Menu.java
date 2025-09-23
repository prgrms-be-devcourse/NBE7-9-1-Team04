package com.backend.domain.menu.entity;

import com.backend.domain.cart.entity.Cart;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menu")
@Getter
@NoArgsConstructor
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    //메뉴 ID
    private Long menu_id;

    //메뉴 이름
    @Column(nullable = false, length = 255)
    private String name;

    //메뉴 가격
    @Column(nullable = false)
    private int price;

    //품절 여부
    @Column(nullable = false)
    private Boolean is_sold_out;

    //메뉴 설명
    @Column(columnDefinition = "TEXT")
    private String description;

    //메뉴 이미지 URL
    @Column(length = 255)
    private String image_url;

    //생성 시간
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    //수정 시간
    @UpdateTimestamp
    private LocalDateTime updated_at;

    // Cart와의 일대다 양방향 관계 설정 (아직 장바구니 엔티티가 없으므로 주석 처리)
//    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
//    @Builder.Default
//    private List<Cart> carts = new ArrayList<>();

    public Menu(String name, int price, Boolean is_sold_out, String description, String image_url) {
        this.name = name;
        this.price = price;
        this.is_sold_out = is_sold_out;
        this.description = description;
        this.image_url = image_url;
    }

}
