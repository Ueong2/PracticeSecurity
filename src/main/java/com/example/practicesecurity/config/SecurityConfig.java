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
                .requestMatchers("/", "/login","/loginProc","/join","/joinProc").permitAll()    // permitAll : 로그인 하지 않아도 접근 가능한 메서드
                .requestMatchers("/admin").hasRole("ADMIN")                                     // hasRole : ADMIN이라는 Role이 있을 시 접근 가능한 메서드
                .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")                   // hasAnyRole : 여러 Role 설정 가능 메서드
                .anyRequest().authenticated()                                                            // anyRequest : 나머지 경로에 대한 권한 부여 메서드
                );
                    // authenticated : 로그인 시 접근할 수 있는 메서드
                    // denyAll : 로그인해도 접근할 수 없는 메서드

        http
                .formLogin((auth) -> auth.loginPage("/login")       // login페이지 설정, admin 페이지 접근 시 오류 대신 해당 페이지로 redirect 해줌
                .loginProcessingUrl("/loginProc")                   // 로그인 데이터를 특정한 경로로 보냄
                .permitAll()
                );

        http
                .csrf((auth) -> auth.disable());        // 사이트 위 변조 방지 설정, 현재 disable한 이유는 로그인 post요청을 보낼 때
                                                        // csrf 토큰도 보내주어야 로그인이 진행되는데 현재의 개발 환경에서는 보내줄 수 없기 때문

        http
                .sessionManagement((auth) -> auth
                .maximumSessions(1)                     // 하나의 아이디에 대한 다중 로그인 허용 갯수
                .maxSessionsPreventsLogin(true));       // true : 최대 허용 로그인 수 초과하는 경우 새로운 로그인 차단
                                                        // false : 최대 허용 로그인 수 초과하는 경우 기존 로그인 계정을 로그아웃(기존 세션 삭제)

        http
                .sessionManagement((auth) -> auth
                .sessionFixation().changeSessionId());
                // none() : 로그인 시 세션 정보 변경 안함
                // newSession() : 로그인 시 세션 새로 생성
                // changeSessionId() : 로그인 시 동일한 세션에 대한 id 변경

        return http.build();
    }
}