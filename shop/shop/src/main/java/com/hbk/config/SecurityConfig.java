package com.hbk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())

                .authorizeHttpRequests(auth -> auth
                        // ✅ 이 API는 로그인 없이 허용
                        .requestMatchers("/api/product-images").permitAll()

                        // (개발 중엔 아래도 같이 열어두면 편함)
                        .requestMatchers("/", "/error").permitAll()

                        // 나머지는 일단 막아도 되고, 개발 중이면 permitAll로 풀어도 됨
                        .anyRequest().authenticated()
                )

                // 기본 로그인 폼 사용
                .formLogin(Customizer.withDefaults());

        return http.build();
    }
}