package com.sparta.team2project.commons.jwt;


import com.sparta.team2project.users.UserRoleEnum;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {
    private static final String AUTHORIZATION_HEADER = "Authorization"; // 헤더의 키값
    public static final String AUTHORIZATION_KEY = "auth"; // 토큰의 키값
    public static final String BEARER_PREFIX = "Bearer"; // 토큰 접두사
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분 설정

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct// 객체 생성 후 초기화를 수행하는 메서드
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    //토큰 생성
    public String createToken(String username, UserRoleEnum role) {

    }
}
