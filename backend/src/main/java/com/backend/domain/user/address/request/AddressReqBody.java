package com.backend.domain.user.address.request;

public record AddressReqBody(
        String address,
        String addressDetail,
        String postNumber
) {
}
