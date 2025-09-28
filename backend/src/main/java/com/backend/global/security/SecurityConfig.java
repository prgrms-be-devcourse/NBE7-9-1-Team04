package com.backend.global.security;

import com.backend.domain.user.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.mysql.cj.conf.PropertyKey.logger;

@Controller
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomAuthenticationFilter customAuthenticationFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/favicon.ico").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers( // 스웨거 관련 열어주기
                                "/swagger-ui/",
                                "/swagger-ui/index.html",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/webjars/**"
                        ).permitAll()
                        .requestMatchers( // 아래는 로그인 안되어도 허용할 URL 경로들
                                HttpMethod.GET,
                                "/api/"
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/users/join",
                                "/api/users/login"
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.DELETE
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/users/logout"
                        ).permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .headers(headers -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN
                        )))
                .addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(
                        exceptionHandling -> exceptionHandling
                                .authenticationEntryPoint((request, response, e) -> {
                                    response.setContentType("application/json;charset=UTF-8");
                                    response.setStatus(401);
                                    response.getWriter().write("""
                                            {
                                                "code" : "U007",
                                                "message" : "로그인되어 있지 않습니다. 로그인 해 주십시오."
                                            }
                                            """);

                                })
                                .accessDeniedHandler((request, response, accessDeniedException) -> {
                                    response.setContentType("application/json;charset=UTF-8");
                                    response.setStatus(403);
                                    response.getWriter().write("""
                                            {
                                                "code" : "M003",
                                                "message" : "관리자 권한이 필요합니다."
                                            }
                                            """);
                                })
                );

        return http.build();
    }
}
