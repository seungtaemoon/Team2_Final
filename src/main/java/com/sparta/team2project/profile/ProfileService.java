package com.sparta.team2project.profile;

import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.profile.dto.ProfileRequestDto;
import com.sparta.team2project.profile.dto.ProfileResponseDto;
import com.sparta.team2project.users.UserRepository;
import com.sparta.team2project.users.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;


    // 프로필 조회하기
    public ResponseEntity<ProfileResponseDto> getProfile(Users users) {
//        Users findUser = userRepository.findByEmail(users.getEmail())
//                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Profile findProfile = profileRepository.findByUsers_Email(users.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("마이페이지를 찾을 수 없습니다."));

        ProfileResponseDto responseDto = new ProfileResponseDto(findProfile);

        return ResponseEntity.ok(responseDto);
    }

    // 프로필 수정하기(닉네임, 프로필이미지)
    @Transactional
    public ResponseEntity<MessageResponseDto> updateProfile(ProfileRequestDto requestDto, Users users) {
//        Users findUser = userRepository.findByEmail(users.getEmail())
//                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        // 프로필 조회
        Profile findProfile = profileRepository.findByUsers_Email(users.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("프로필을 찾을 수 없습니다."));

        // 사용자와 프로필 이메일이 같은지 확인
        if (!findProfile.getUsers().getEmail().equals(users.getEmail())) {
            throw new IllegalArgumentException("마이페이지 수정 권한이 없습니다.");
        }

        //닉네임, 프로필이미지 업데이트
        findProfile.updateProfile(requestDto);
        profileRepository.save(findProfile);

        MessageResponseDto responseDto = new MessageResponseDto("프로필 수정 성공", 200);
        return ResponseEntity.ok(responseDto);
    }

    //프로필 수정하기(비밀번호)
    @Transactional
    public ResponseEntity<MessageResponseDto> updatePassword(ProfileRequestDto requestDto, Users users) {
        Users findUser = userRepository.findByEmail(users.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        // 프로필 조회
        Profile findProfile = profileRepository.findByUsers_Email(users.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("프로필을 찾을 수 없습니다."));

        // 사용자와 프로필 이메일이 같은지 확인
        if (!findProfile.getUsers().getEmail().equals(users.getEmail())) {
            throw new IllegalArgumentException("마이페이지 수정 권한이 없습니다.");
        }

        // 현재 비밀번호 확인
        String currentPassword = requestDto.getCurrentPassword();
        if (!passwordEncoder.matches(currentPassword, findProfile.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }
        // 새로운 비밀번호 업데이트
        String updatePassword = requestDto.getUpdatePassword();

        findProfile.updatePassword(requestDto);

        profileRepository.save(findProfile);

        MessageResponseDto responseDto = new MessageResponseDto("비밀번호 수정 성공", 200);
        return ResponseEntity.ok(responseDto);
    }
}
