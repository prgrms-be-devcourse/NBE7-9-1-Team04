package com.backend.domain.user.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, length = 100)
    private String email;
    @Column(length = 20, nullable = false)
    private String password;
    @Column(length = 15, nullable = false)
    private String phoneNumber;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private int level;

    public User(Long id, String email, String password, String phoneNumber, int level) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.level = level;
        this.createDate = LocalDateTime.now();
        this.modifyDate = LocalDateTime.now();
    }
}
