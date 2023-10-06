package com.sparta.team2project.commons;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity // Spring Security를 설정할 클래스라고 정의
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JWT
}
