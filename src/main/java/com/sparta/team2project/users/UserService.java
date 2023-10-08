package com.sparta.team2project.users;

import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.exceptionhandler.CustomException;
import com.sparta.team2project.commons.exceptionhandler.ErrorCode;
import com.sparta.team2project.profile.Profile;
import com.sparta.team2project.profile.ProfileRepository;
import com.sparta.team2project.users.dto.SignoutRequestDto;
import com.sparta.team2project.users.dto.SignupRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;

    // ADMIN_TOKEN
    @Value("${ADMIN_TOKEN}")
    private String ADMIN_TOKEN;

    public ResponseEntity<MessageResponseDto> signup(SignupRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<Users> checkUserId = userRepository.findByEmail(email);
        if (checkUserId.isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
        }

        // 사용자 ROLE 확인
        UserRoleEnum userRole = UserRoleEnum.USER;

        if (requestDto.getAdminToken() != null && ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
            userRole = UserRoleEnum.ADMIN; // adminToken이 제공되면 ADMIN으로 설정

        }

        // 기본값 설정
        String nickName = "익명";
        String profileImg = "https://blog.kakaocdn.net/dn/ckw6CM/btsxrWYLmoZ/IW4PRNSDLAWNZEKkZO0qM1/img.png";

        // 입력값이 존재한다면 기본값 대체
        if (requestDto.getNickName() != null) {
            nickName = requestDto.getNickName();
        }
        if (requestDto.getProfileImg() != null) {
            profileImg = requestDto.getProfileImg();
        }

        // 사용자 등록
        Users users = new Users(email, nickName, password, userRole, profileImg);
        userRepository.save(users);
        // 프로필 생성
        Profile profile = new Profile(users, users.getPassword(), users.getNickName(), users.getProfileImg());
        profileRepository.save(profile);

        return ResponseEntity.ok(new MessageResponseDto("회원가입 완료", HttpStatus.CREATED.value()));
    }



    // 회원탈퇴
    public ResponseEntity<MessageResponseDto> deleteUser(SignoutRequestDto requestDto, String email) {
        Users users = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("등록된 아이디가 없습니다.")
        );
        if (!passwordEncoder.matches(requestDto.getPassword(), users.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        userRepository.delete(users);
        return ResponseEntity.ok(new MessageResponseDto("회원탈퇴 완료", HttpStatus.OK.value()));

    }
}
