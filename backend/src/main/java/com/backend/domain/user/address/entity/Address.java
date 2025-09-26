package com.backend.domain.user.address.entity;

import com.backend.domain.user.address.dto.AddressDto;
import com.backend.domain.user.user.entity.Users;
import com.backend.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Address extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    private String address;

    private String addressDetail;

    private String postNumber;

    public Address(Users user, AddressDto addressDto) {
        this.user = user;
        this.address = addressDto.address();
        this.addressDetail = addressDto.addressDetail();
        this.postNumber = addressDto.postNumber();
    }

    public Address changeAddress(AddressDto addressDto) {
        this.address = addressDto.address();
        this.addressDetail = addressDto.addressDetail();
        this.postNumber = addressDto.postNumber();
        return this;
    }
}
