package com.example.PracticeSecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/login").permitAll()     // permitAll : 로그인 하지 않아도 접근 가능한 메서드
                        .requestMatchers("/admin").hasRole("ADMIN")     // hasRole : ADMIN이라는 Role이 있을 시 접근 가능한 메서드
                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")      // hasAnyRole : 여러 Role 설정 가능 메서드
                        .anyRequest().authenticated()   // anyRequest : 나머지 경로에 대한 권한 부여 메서드
                );
        // authenticated : 로그인 시 접근할 수 있는 메서드
        // denyAll : 로그인해도 접근할 수 없는 메서드

        return http.build();
    }
}