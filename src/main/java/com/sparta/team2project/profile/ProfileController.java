package com.sparta.team2project.profile;

import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.security.UserDetailsImpl;
import com.sparta.team2project.profile.dto.ProfileRequestDto;
import com.sparta.team2project.profile.dto.ProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/profile")
public class ProfileController {

    private final ProfileService profileService;

    // 마이페이지 조회하기
    @GetMapping()
    public ResponseEntity<ProfileResponseDto> getProfile (@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return profileService.getProfile(userDetails.getUsers());
    }

    // 프로필 수정하기(닉네임, 프로필이미지)
    @PutMapping()
    public ResponseEntity<MessageResponseDto> updateProfile(@RequestBody ProfileRequestDto requestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return profileService.updateProfile(requestDto, userDetails.getUsers());
    }

    //프로필 수정하기(비밀번호)
    @PutMapping("/update-password")
    public ResponseEntity<MessageResponseDto> updatePassword(@RequestBody ProfileRequestDto requestDto,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return profileService.updatePassword(requestDto, userDetails.getUsers());
    }
}
