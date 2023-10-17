package com.sparta.team2project.users.social.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.jwt.JwtUtil;
import com.sparta.team2project.commons.security.UserDetailsServiceImpl;
import com.sparta.team2project.users.UserService;
import com.sparta.team2project.users.social.dto.TokenDto;
import com.sparta.team2project.users.social.service.KakaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class KakaoController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final KakaoService kakaoService;

    @Operation(summary = "카카오 회원가입입니다", description = "카카오 회원가입 api 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "bad request operation", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))
    })

    @GetMapping("/kakao/callback")
    public ResponseEntity<String> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        // code: 카카오 서버로부터 받은 인가 코드 Service 전달 후 인증 처리 및 토큰 반환
        TokenDto tokenDto = kakaoService.kakaoLogin(code, response);

        return ResponseEntity.ok("성공");
    }
}
