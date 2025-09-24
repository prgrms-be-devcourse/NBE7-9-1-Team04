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
    private Long user_id;
    @Column(unique = true, nullable = false, length = 100)
    private String email;
    @Column(length = 20, nullable = false)
    private String password;
    @Column(length = 15, nullable = false)
    private String phone_number;
    private LocalDateTime create_date;
    private LocalDateTime modify_date;
    private int level;

    public User(String email, String password, String phoneNumber, int level) {
        this.email = email;
        this.password = password;
        this.phone_number = phoneNumber;
        this.level = level;
        this.create_date = LocalDateTime.now();
        this.modify_date = LocalDateTime.now();
    }
}
