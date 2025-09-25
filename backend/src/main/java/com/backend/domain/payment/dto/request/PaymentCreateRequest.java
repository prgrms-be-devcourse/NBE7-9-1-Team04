package com.backend.domain.payment.dto.request;

import com.backend.domain.order.entity.Orders;
import com.backend.domain.payment.entity.Payment;
import com.backend.domain.payment.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/*
 * 결제 요청 DTO
 */
public record PaymentCreateRequest(
        @NotNull(message = "주문 ID는 필수입니다.")
        @Positive(message = "주문 ID는 양수여야 합니다.")
        Long orderId,

        @Positive(message = "결제 금액은 양수여야 합니다.")
        int paymentAmount,

        @NotNull(message = "결제 방법은 필수입니다.")
        PaymentMethod paymentMethod
) {
    public Payment createPayment(Orders orders) {
        return Payment.builder()
                .paymentAmount(paymentAmount)
                .paymentMethod(paymentMethod)
                .orders(orders)
                .build();
    }
}
