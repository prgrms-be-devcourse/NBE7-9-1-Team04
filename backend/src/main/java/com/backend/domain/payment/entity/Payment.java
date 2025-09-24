package com.backend.domain.payment.entity;

import com.backend.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 결제 테이블 키
    private Long paymentKey;

    // 결제 금액, NOT NULL
    @Column(nullable = false)
    private int order_amount;

    // 결제 유형, NOT NULL [ex: 카드 결제]
    @Column(nullable = false, length = 30)
    private String order_paymentType;

    // 결제 상태, NOT NULL [결제 전 / 결제 완료]
    @Column(nullable = false, length = 20, columnDefinition = "VARCHAR(20) DEFAULT '결제 전'")
    private String order_status;
}
