package com.sparta.team2project.commons.jwt;

import com.sparta.team2project.commons.entity.UserRoleEnum;
import com.sparta.team2project.refreshToken.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Stack;

@Slf4j(topic = "JwtUtil")
@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final RefreshTokenRepository refreshTokenRepository;

    public static final String AUTHORIZATION_HEADER = "Authorization"; // 헤더의 키값
    public static final String REFRESH_HEADER = "refresh"; // 토큰의 키값
    public static final String BEARER_PREFIX = "Bearer "; // 토큰 접두사
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String REFRESH_TOKEN = "refresh_token"; // 리프레시 토큰

    private final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L; // 60분 설정
    private final long REFRESH_TOKEN_TIME = 2 * 7 * 24 * 60 * 60 * 1000L; // 14일 설정

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct// 객체 생성 후 초기화를 수행하는 메서드
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }


    // header 토큰을 가져오기
    public String resolveToken(HttpServletRequest request, String header) {
        String bearerToken = request.getHeader(header);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }


    //토큰 생성 (email 기준! )
    public String createToken(String email, UserRoleEnum role) {
        Date date = new Date();
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(email) // 사용자 식별자값
                        .claim(AUTHORIZATION_KEY , role) // 사용자 권한
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME)) // 토큰 만료 시간
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }
    public String createRefreshToken(String email, UserRoleEnum role) {
        Date date = new Date();
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(email) // 사용자 식별자값
                        .claim(AUTHORIZATION_KEY , role) // 사용자 권한
                        .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME)) // 토큰 만료 시간
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    //header 에서 token 추출
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String reissueAccessToken(String refreshToken) {
        Claims claims = this.getUserInfoFromToken(refreshToken);
        return this.createToken(claims.getSubject(),
                UserRoleEnum.valueOf(claims.get("auth").toString()));
    }

    public String getUserIdFromExpiredToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
        return null;
    }


    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }


    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
