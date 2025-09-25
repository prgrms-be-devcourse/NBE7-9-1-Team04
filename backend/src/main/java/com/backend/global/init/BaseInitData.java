package com.backend.global.init;

import com.backend.domain.user.user.entity.Users;
import com.backend.domain.user.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {

    private final UserRepository userRepository;

    @Bean
    CommandLineRunner initData() {
        return args -> {

            // 관리자 계정 (level = 0)
            if (userRepository.getUsersByEmail("admin@example.com").isEmpty()) {
                Users admin = new Users(
                        "admin@example.com",
                        "admin1234", // 평문 비밀번호
                        "010-1234-5678",
                        0 // 관리자
                );
                userRepository.save(admin);
                System.out.println("기본 관리자 계정 생성 완료");
            }

            // 일반 사용자 계정 (level = 1)
            if (userRepository.getUsersByEmail("user@example.com").isEmpty()) {
                Users user = new Users(
                        "user@example.com",
                        "user1234", // 평문 비밀번호
                        "010-1234-1111",
                        1 // 일반 사용자
                );
                userRepository.save(user);
                System.out.println("기본 사용자 계정 생성 완료");
            }
        };
    }
}