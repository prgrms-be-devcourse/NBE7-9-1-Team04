package com.backend.domain.payment.entity;

public enum PaymentMethod {
    CARD("카드 결제");

    private final String description;

    PaymentMethod(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
