package com.sparta.team2project.users;

import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.exceptionhandler.ErrorCode;
import com.sparta.team2project.users.dto.SignupRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public ResponseEntity<MessageResponseDto> signup(SignupRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<Users> checkUserId = userRepository.findByEmail(email);
        if (checkUserId.isPresent()) {
            return ResponseEntity.status(ErrorCode.DUPLICATED_EMAIL.getStatusCode())
                    .body(new MessageResponseDto(ErrorCode.DUPLICATED_EMAIL.getMsg(), ErrorCode.DUPLICATED_EMAIL.getStatusCode().value()));
        }

        // 사용자 ROLE 확인
        UserRoleEnum userRole = UserRoleEnum.USER;

        if (requestDto.getAdminToken() != null && ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
            userRole = UserRoleEnum.ADMIN; // adminToken이 제공되면 ADMIN으로 설정

        }

        // 사용자 등록
        Users user = new Users(email, password, userRole);
        userRepository.save(user);

        // DB에 중복된 email 이 없다면 회원을 저장하고 Client 로 성공했다는 메시지, 상태코드 반환하기
        return ResponseEntity.ok(new MessageResponseDto("회원가입 완료", HttpStatus.CREATED.value()));
    }

// 회원탈퇴
//    public ResponseEntity<String> deleteUser(SignoutRequestDto requestDto, String userId) {
//        Users user = userRepository.findByUserId(userId).orElseThrow(
//                () -> new IllegalArgumentException("등록된 아이디가 없습니다.")
//        );
//        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
//            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//        }
//        userRepository.delete(user);
//        return ResponseEntity.ok("회원탈퇴가 완료되었습니다.");
//
//    }
}
