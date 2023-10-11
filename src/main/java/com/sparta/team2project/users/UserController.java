package com.sparta.team2project.users;

import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.security.UserDetailsImpl;
import com.sparta.team2project.email.dto.EmailRequestDto;
import com.sparta.team2project.users.dto.SignoutRequestDto;
import com.sparta.team2project.users.dto.SignupRequestDto;

import com.sparta.team2project.email.dto.ValidNumberRequestDto;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    // 회원가입(이메일 가입)
    @PostMapping("/signup")
    public ResponseEntity<MessageResponseDto> signup(@RequestBody SignupRequestDto signupRequestDto) {
        return userService.signup(signupRequestDto);
    }

    // 이메일 요청 보내기
    @PostMapping("/signup/email")
    public ResponseEntity<MessageResponseDto> sendEmail(@RequestBody @Valid EmailRequestDto emailRequestDto) {
        return userService.checkEmail(emailRequestDto.getEmail());
    }

    // 인증번호 확인하기
    @PostMapping("/signup/email/valid")
    public ResponseEntity<Boolean> checkValidNumber(@RequestBody ValidNumberRequestDto validNumberRequestDto) {
        boolean checkNumber = userService.checkValidNumber(validNumberRequestDto, validNumberRequestDto.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(checkNumber);
    }


    // 회원 탈퇴
    @DeleteMapping("/signout")
    public ResponseEntity<MessageResponseDto> deleteUser(@RequestBody SignoutRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.deleteUser(requestDto, userDetails.getEmail());
    }


}
