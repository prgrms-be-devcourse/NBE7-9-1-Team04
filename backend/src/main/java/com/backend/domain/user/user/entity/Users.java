package com.backend.domain.user.user.entity;

import com.backend.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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

    public boolean isMatchedPassword(String password) {
        return this.password.equals(password);
    }
}
