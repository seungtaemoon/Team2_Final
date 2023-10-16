package com.sparta.team2project.commons.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.entity.UserRoleEnum;
import com.sparta.team2project.commons.jwt.JwtUtil;
import com.sparta.team2project.users.UserRepository;
import com.sparta.team2project.users.Users;
import com.sparta.team2project.users.dto.LoginRequestDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        setFilterProcessesUrl("/api/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");
        try {
            // 요청 본문이 비어 있는지 확인
            if (request.getContentLength() == 0) {
                throw new RuntimeException("요청 본문이 비어 있습니다.");
            }

            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException("요청 처리 중 오류가 발생했습니다.");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        log.info("로그인 성공 및 JWT 생성");

        String email = ((UserDetailsImpl) authResult.getPrincipal()).getUsers().getEmail(); // username -> email
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUsers().getUserRole();

        String token = jwtUtil.createToken(email, role); // username -> email
        response.addHeader("Authorization", token);

        MessageResponseDto responseDto = new MessageResponseDto("로그인 성공", 200);

        // 응답 데이터 설정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

        // JSON 변환 후 출력
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), responseDto);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        log.info("로그인 실패");
        response.setStatus(401);

        String errorMessage = "로그인 실패";
        if (failed instanceof BadCredentialsException) {
            // 이메일이 존재하는지 확인
            String email = request.getParameter("email");
            Optional<Users> user = userRepository.findByEmail(email);
            if (!user.isPresent()) {
                errorMessage = "해당 사용자를 찾을 수 없습니다.";
            } else {
                errorMessage = "잘못된 비밀번호입니다.";
            }
        }


        MessageResponseDto responseDto = new MessageResponseDto(errorMessage, 400);

        // 응답 데이터 설정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

        // JSON 변환 후 출력
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), responseDto);
    }


}
