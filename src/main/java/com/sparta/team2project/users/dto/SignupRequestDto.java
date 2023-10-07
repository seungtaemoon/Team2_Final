package com.sparta.team2project.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignupRequestDto {

    @Email // 이메일 형식이어야 함
    @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*]{8,15}$", message = "비밀번호는 대소문자, 숫자, 특수문자( !@#$%^&* )로만 구성된 8~15자의 문자열이어야 합니다.")
    private String password;


    @NotBlank
    private String nickName; // 추가된 부분

    @NotBlank
    private String profileImg; // 추가된 부분

    private boolean admin = false;
    private String adminToken = "";
}
