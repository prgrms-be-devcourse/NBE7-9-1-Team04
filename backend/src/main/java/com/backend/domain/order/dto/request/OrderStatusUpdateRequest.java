package com.backend.domain.order.dto.request;

import jakarta.validation.constraints.NotNull;

public record OrderStatusUpdateRequest(
        @NotNull
        String newStatus
) {
}

