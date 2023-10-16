package com.sparta.team2project.users.social.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.team2project.users.social.service.KakaoService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class KakaoController {
    private final KakaoService kakaoService;

    // 카카오 로그인
    @GetMapping("/kakao/callback")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        return kakaoService.kakaoLogin(code, response);
    }
}
