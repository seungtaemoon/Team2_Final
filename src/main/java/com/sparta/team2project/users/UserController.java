package com.sparta.team2project.users;

import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.security.UserDetailsImpl;
import com.sparta.team2project.users.dto.SignoutRequestDto;
import com.sparta.team2project.users.dto.SignupRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<MessageResponseDto> signup(@RequestBody SignupRequestDto signupRequestDto) {
        return userService.signup(signupRequestDto);
    }

    // 회원 탈퇴
    @DeleteMapping("signout")
    public ResponseEntity<MessageResponseDto> deleteUser(@RequestBody SignoutRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.deleteUser(requestDto, userDetails.getEmail());
    }
}
