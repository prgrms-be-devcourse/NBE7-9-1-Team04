package com.backend.global.init;

import com.backend.domain.menu.entity.Menu;
import com.backend.domain.menu.repository.MenuRepository;
import com.backend.domain.user.user.entity.Users;
import com.backend.domain.user.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {

    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initData() {
        return args -> {

            // 관리자 계정 (level = 0)
            if (userRepository.getUsersByEmail("admin@example.com").isEmpty()) {
                Users admin = new Users(
                        "admin@example.com",
                        passwordEncoder.encode("admin1234"), // 암호화 비밀번호
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
                        passwordEncoder.encode("user1234"), // 평문 비밀번호
                        "010-1234-1111",
                        1 // 일반 사용자
                );
                userRepository.save(user);
                System.out.println("기본 사용자 계정 생성 완료");
            }

            //기본 메뉴 생성
            if (menuRepository.count() == 0) {
                menuRepository.save(new Menu(
                        "에티오피아 예가체프 G1",
                        25000,
                        false,
                        "산미와 화사한 꽃 향기가 특징인 스페셜티 원두",
                        "https://i.postimg.cc/GHpyNHcW/ethiopia.jpg"
                ));

                menuRepository.save(new Menu(
                        "콜롬비아 수프리모",
                        20000,
                        false,
                        "부드러운 바디감과 고소한 견과류 향의 대표적인 원두",
                        "https://i.postimg.cc/JGY04s7v/colombia.jpg"
                ));

                menuRepository.save(new Menu(
                        "케냐 AA",
                        28000,
                        false,
                        "묵직한 바디감과 와인 같은 풍미가 일품인 고급 원두",
                        "https://i.postimg.cc/w3R0fjQF/kenya.jpg"
                ));

                menuRepository.save(new Menu(
                        "브라질 산토스",
                        18000,
                        false,
                        "균형 잡힌 맛과 부드러운 산미의 대중적인 원두",
                        "https://i.postimg.cc/SJxjh64c/brazil.jpg"
                ));

                menuRepository.save(new Menu(
                        "품절 메뉴",
                        25000,
                        true,
                        "산미와 화사한 꽃 향기가 특징인 스페셜티 원두",
                        "https://i.postimg.cc/GHpyNHcW/ethiopia.jpg"
                ));
            }
        };
    }
}