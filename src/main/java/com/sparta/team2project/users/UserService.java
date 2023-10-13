package com.sparta.team2project.users;

import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.entity.UserRoleEnum;
import com.sparta.team2project.commons.exceptionhandler.CustomException;
import com.sparta.team2project.commons.exceptionhandler.ErrorCode;
import com.sparta.team2project.email.EmailService;
import com.sparta.team2project.email.ValidNumber.ValidNumber;
import com.sparta.team2project.email.ValidNumber.ValidNumberRepository;
import com.sparta.team2project.email.dto.ValidNumberRequestDto;
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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final ValidNumberRepository validNumberRepository;

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
        // 비밀번호 유효성 검사
        if (!isValidPassword(requestDto.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
        // 닉네임 유효성 검사
        String nickName = requestDto.getNickName();
        if (nickName == null) {
            nickName = "익명"; // 닉네임이 null인 경우 "익명"으로 처리
        } else if (!isValidNickName(nickName)) {
            throw new CustomException(ErrorCode.INVALID_NICKNAME);
        }

        String profileImg = "https://blog.kakaocdn.net/dn/ckw6CM/btsxrWYLmoZ/IW4PRNSDLAWNZEKkZO0qM1/img.png";
        if (requestDto.getProfileImg() != null) {
            profileImg = requestDto.getProfileImg();
        }
        // 인증한 이메일과 같은 이메일인지 확인
        if (!email.equals(validNumberRepository.findByEmail(email).get().getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_NOT_VALIDATED); // 인증한 이메일과 다른 이메일을 사용한 경우 에러 처리
        }
//        // 이메일 인증 확인 (로그인 힘들어지니까 나중에 추가. request값 변경 필요. 현재는 인증 번호 확인 메서드 사용 중)
//        boolean isEmailVerified = checkValidNumber(requestDto.getValidNumber(), email); // 여기서 인증번호 확인
//        if (!isEmailVerified) {
//            throw new CustomException(ErrorCode.EMAIL_VERIFICATION_FAILED);
//        }
        // 사용자 등록
        Users users = new Users(email, nickName, password, userRole, profileImg);
        userRepository.save(users);
        // 프로필 생성
        Profile profile = new Profile(users);
        profileRepository.save(profile);

        return ResponseEntity.ok(new MessageResponseDto("회원가입 완료", HttpStatus.CREATED.value()));
    }


    // 인증번호 요청
    public ResponseEntity<MessageResponseDto> checkEmail(String email) {

        //해당 이메일로 전에 인증번호를 요청했다면 전 인증번호를 db에서 삭제함
        Optional<ValidNumber> pastNumber = validNumberRepository.findByEmail(email);
        if(pastNumber.isPresent()){
            validNumberRepository.delete(pastNumber.get());
        }

        Optional<Users> checkUsers = userRepository.findByEmail(email);

        if(checkUsers.isPresent()){
            throw new CustomException(ErrorCode.DUPLICATED_EMAIL); //이메일로 인증번호를 찾을 수 없음
        }
        if (email == null) {
            throw new CustomException(ErrorCode.EMAIL_FORMAT_WRONG); // 이메일 형식이 잘못됨
        }
        // 인증한 이메일이 가입시 사용할 이메일이어야 함. -> 회원가입 기능에서 ValidNumber.email과 Users.email이 일치해야 함.


        int number = (int)(Math.random() * 899999) + 100000; // 6자리 난수 생성(인증번호)

        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss"); // 현재시간에서 HHmmss 형식으로 시간을 가져옴
        long formatedNow = Long.parseLong(now.format(formatter));

        ValidNumber validNumber = new ValidNumber(number, email, formatedNow);//받는 값
        validNumberRepository.save(validNumber);

        emailService.sendNumber(number, email);
        return ResponseEntity.ok((new MessageResponseDto("인증번호가 발송되었습니다.", HttpStatus.OK.value())));
    }


    // 인증 번호 확인하기
    public boolean checkValidNumber(ValidNumberRequestDto validNumberRequestDto, String email) {
        boolean checkNumber = true;

        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss"); // 현재시간에서 HHmmss 형식으로 시간을 가져옴.
        long formatedNow = Long.parseLong(now.format(formatter)); //

        Optional<ValidNumber> validNumberEmail = validNumberRepository.findByEmail(email);

        if(!validNumberEmail.isPresent()){
            throw new CustomException(ErrorCode.INVALID_VALID_TOKEN); //이메일로 인증번호를 찾을 수 없음
        }

        ValidNumber validNumber = validNumberEmail.get();
        double time = validNumber.getTime();

        if(formatedNow - time >= 300){ // 인증번호 발급 받은지 3분 초과, formatedNow : 현재시간, time : 인증번호 발급 시간
            throw new CustomException(ErrorCode.VALID_TIME_OVER);
        }
//        number != validNumber.getValidNumber()
        if(validNumber.getValidNumber() != validNumberRequestDto.getValidNumber()){
            throw new CustomException(ErrorCode.WRONG_NUMBER);
        }else{
            return checkNumber; // 인증성공시 true 값이 반환됩니다.
        }
    }


    // 회원탈퇴(연관관게 설정 필요) - 미완
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



    private boolean isValidPassword(String password) {
        // 비밀번호는 대소문자, 숫자, 특수문자( !@#$%^&* )로만 구성된 8~15자의 문자열이어야 합니다.
        String passwordRegex = "^[a-zA-Z0-9!@#$%^&*]{8,15}$";
        return password.matches(passwordRegex);
    }

    private boolean isValidNickName(String nickName) {
        // 닉네임은 2글자 이상, 10글자 이하의 영문자, 숫자, 또는 한글로만 구성되어야 합니다.
        String nickNameRegex = "^[A-Za-z0-9가-힣]{2,10}$";
        return nickName.matches(nickNameRegex);
    }

}
