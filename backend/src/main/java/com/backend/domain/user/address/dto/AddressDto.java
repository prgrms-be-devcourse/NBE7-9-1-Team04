package com.backend.domain.user.address.dto;

import com.backend.domain.user.address.entity.Address;
import com.backend.domain.user.address.dto.request.AddressReqBody;

public record AddressDto(
        Long addressId,
        Long userId,
        String address,
        String addressDetail,
        String postNumber
) {
    public AddressDto(Address address) {
        this(
                address.getAddressId(),
                address.getUser().getUserId(),
                address.getAddress(),
                address.getAddressDetail(),
                address.getPostNumber()
        );
    }

    public AddressDto(AddressReqBody addressReqBody) {
        this(
                null,
                null,
                addressReqBody.address(),
                addressReqBody.addressDetail(),
                addressReqBody.postNumber()
        );
    }
}
