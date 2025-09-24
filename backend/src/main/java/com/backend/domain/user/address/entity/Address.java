package com.backend.domain.user.address.entity;

import com.backend.domain.user.user.entity.Users;
import com.backend.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
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

}
