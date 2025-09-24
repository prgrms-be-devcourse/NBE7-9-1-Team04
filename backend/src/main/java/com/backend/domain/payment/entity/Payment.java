package com.backend.domain.payment.entity;

import com.backend.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 결제 테이블 키
    private Long paymentId;

    // 결제 금액, NOT NULL
    @Column(nullable = false)
    private int orderAmount;

    // 결제 유형, NOT NULL [ex: 카드 결제]
    @Column(nullable = false, length = 30)
    private String orderPaymentType;

    // 결제 상태, NOT NULL [결제 전 / 결제 완료]
    @Column(nullable = false, length = 20)
    private String orderStatus;

    public Payment(int orderAmount, String orderPaymentType) {
        this.orderAmount = orderAmount;
        this.orderPaymentType = orderPaymentType;
        this.orderStatus = "결제 전";
    }
}
