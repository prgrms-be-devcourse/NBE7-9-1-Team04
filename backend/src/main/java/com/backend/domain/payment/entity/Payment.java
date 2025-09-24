package com.backend.domain.payment.entity;

import com.backend.domain.order.entity.Orders;
import com.backend.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 결제 테이블 키
    private Long paymentId;

    // 결제 금액, NOT NULL
    @Column(nullable = false)
    private int paymentAmount;

    // 결제 유형, NOT NULL [ex: 카드 결제]
    @Column(nullable = false, length = 30)
    private String paymentMethod;

    // 결제 상태, NOT NULL [결제 전 / 결제 완료]
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus paymentStatus;

    // 주문 엔티티와 연결 관계, OneToOne
    @OneToOne(mappedBy = "payment", fetch = FetchType.LAZY)
    private Orders orders;

    @Builder
    public Payment(int paymentAmount, String paymentMethod, Orders orders) {
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = PaymentStatus.PENDING;
        this.orders = orders;
    }
}
