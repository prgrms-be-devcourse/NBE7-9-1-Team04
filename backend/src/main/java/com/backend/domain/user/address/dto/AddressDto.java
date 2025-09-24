package com.backend.domain.user.address.dto;

import com.backend.domain.user.address.entity.Address;

public record AddressDto(
        Long addressId,
        Long userId,
        String address,
        String addressDetail,
        String postNumber
) {
    AddressDto(Address address) {
        this(
                address.getAddressId(),
                address.getUser().getUserId(),
                address.getAddress(),
                address.getAddressDetail(),
                address.getPostNumber()
        );
    }
}
