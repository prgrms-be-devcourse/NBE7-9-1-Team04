package com.backend.domain.user.user.entity;

import com.backend.domain.user.address.entity.Address;
import com.backend.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Entity
@Getter

@NoArgsConstructor
public class Users extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(unique = true, nullable = false, length = 100)
    private String email;
    @Column(length = 20, nullable = false)
    private String password;
    @Column(length = 15, nullable = false)
    private String phoneNumber;
    private int level;
    private String apiKey;

    @OneToMany(mappedBy = "user",  fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE} , orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    public Users(String email, String password, String phoneNumber, int level) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.level = level;
        this.apiKey = UUID.randomUUID().toString();
    }

    public String changeApiKey() throws Exception {
        String newApiKey = UUID.randomUUID().toString();
        this.apiKey = newApiKey;
        return newApiKey;
    }

    public Users changePhoneNumber(String newNumber) throws Exception {
        this.phoneNumber = newNumber;
        return this;
    }

    public Address addAddress(Address address) {
        this.addresses.add(address);
        return address;
    }

    public boolean isMatchedPassword(String password) {
        return this.password.equals(password);
    }

    public Optional<Address> getAddress(Long addressId) {
        for(Address address : this.addresses) {
            if(address.getAddressId().equals(addressId)) {
                return Optional.of(address);
            }
        }
        return Optional.empty();
    }
}
