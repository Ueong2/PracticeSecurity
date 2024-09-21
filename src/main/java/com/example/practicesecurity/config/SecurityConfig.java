package com.example.practicesecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){   // bcrypt 해시 함수를 사용하는 암호화 메서드
        return new BCryptPasswordEncoder();
    }

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

        http
                .formLogin((auth) -> auth.loginPage("/login")       // login페이지 설정, admin 페이지 접근 시 오류 대신 해당 페이지로 redirect 해줌
                        .loginProcessingUrl("/loginProc")           // 로그인 데이터를 특정한 경로로 보냄
                        .permitAll()
                );

        http
                .csrf((auth) -> auth.disable());                    // 사이트 위 변조 방지 설정, 현재 disable한 이유는 로그인 post요청을 보낼 때
                                                                    // csrf 토큰도 보내주어야 로그인이 진행되는데 현재의 개발 환경에서는 보내줄 수 없기 때문

        return http.build();
    }
}