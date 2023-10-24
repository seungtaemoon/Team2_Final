package com.sparta.team2project.profile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.entity.UserRoleEnum;
import com.sparta.team2project.commons.exceptionhandler.CustomException;
import com.sparta.team2project.commons.exceptionhandler.ErrorCode;
import com.sparta.team2project.profile.dto.ProfileNickNameRequestDto;
import com.sparta.team2project.users.UserRepository;
import com.sparta.team2project.users.UserService;
import com.sparta.team2project.users.Users;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.net.MalformedURLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AmazonS3Client amazonS3Client;

    @InjectMocks
    private ProfileService profileService;
    @InjectMocks
    private UserService userService;

    private Users createUserWithProfileImg(String profileImg) {
        Users user = new Users("test@example.com", "TestUser", "password", UserRoleEnum.USER, profileImg);
        return user;
    }

    @Nested
    @DisplayName("GetProfile")
    class GetProfile {
        @Test
        @DisplayName("프로필 조회 성공")
        void getProfileSuccess() {
            // 가짜 User 생성
            Users mockUser = new Users("test@example.com", "TestUser", "password123", UserRoleEnum.USER, "profile.jpg");
            // 가짜 Profile 생성
            Profile mockProfile = new Profile(mockUser);

            // UserRepository 가짜 User 반환하도록 설정, ProfileRepository  가짜 Profile 반환하도록 설정
            when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
            when(profileRepository.findById(mockProfile.getId())).thenReturn(Optional.of(mockProfile));

            // UserRepository에서 User 불러오기, ProfileRepository에서 Profile 불러오기
            Users findUser = userRepository.findById(mockUser.getId()).orElse(null);
            Profile findProfile = profileRepository.findById(mockProfile.getId()).orElse(null);

            // 관계 확인
            assertEquals(findProfile.getUsers(), mockUser);
        }

        @Test
        @DisplayName("프로필 이미지 URL을 읽어오기")
        void readProfileImg() throws MalformedURLException {
            // given
            String profileImg = "https://example.com/profile-image.jpg";
            Users user = createUserWithProfileImg(profileImg);

            // when
            String profileURL = profileService.readProfileImg(user.getId(), user);

            // then
            assertEquals(profileImg, profileURL);
        }

        @Test
        @DisplayName("프로필 조회 실패 - User가 없는 경우")
        void getProfileFailUserNotFound() {
            // given
            Users users = createUserWithProfileImg("profile.jpg");
            String userEmail = users.getEmail();
            // Mock 객체로 userRepository 설정
            when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

            // when
            Throwable exception = assertThrows(CustomException.class, () -> profileService.getProfile(users));

            // then
            assertEquals(ErrorCode.ID_NOT_MATCH, ((CustomException) exception).getErrorCode());
        }

        @Test
        @DisplayName("프로필 조회 실패 - 권한이 없는 경우")
        void getProfileFailCheckAuthority() {
            // given
            Users users = createUserWithProfileImg("profile.jpg");
            String userEmail = users.getEmail();

            // Mock 객체로 userRepository 설정
            when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(users));

            // Mock 객체로 existUser 설정
            Users existUser = new Users("other@example.com", "OtherUser", "otherPassword", UserRoleEnum.USER, "other.jpg");

            //when
            Throwable exception = assertThrows(CustomException.class, () -> profileService.getProfile(users));

            //  then
            assertEquals(ErrorCode.NOT_ALLOWED, ((CustomException) exception).getErrorCode());
        }
    }


    @Nested
    @DisplayName("UpdateProfile")
    class UpdateProfile {
        @Test
        @DisplayName("프로필 수정성공 - 닉네임")
        void updateNickNameSuccess() {
            // given
            Users users = createUserWithProfileImg("profile.jpg");
            Profile profile = new Profile(users);
            ProfileNickNameRequestDto requestDto = new ProfileNickNameRequestDto();

            when(userRepository.findById(users.getId())).thenReturn(Optional.of(users));
            when(profileRepository.findById(profile.getId())).thenReturn(Optional.of(profile));

            // when
            ResponseEntity<MessageResponseDto> response = profileService.updateNickName(requestDto, users);

            // then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("마이페이지 수정 성공", response.getBody().getMsg());
        }

        @Test
        @DisplayName("프로필 수정성공 - 프로필 이미지")
        void updateProfileImgSuccess() {
        }

        @Test
        @DisplayName("프로필 수정성공 - 비밀번호")
        void updatePasswordSuccess() {
        }

        @Test
        @DisplayName("프로필 수정성공 - 자기소개")
        void updateAboutMeSuccess() {

        }

        @Test
        @DisplayName("프로필 조회 실패 - 프로필이 존재하지 않는 경우")
        void getProfileFailCheckProfile() {
            // given
            Users users = createUserWithProfileImg("profile.jpg");
            String userEmail = users.getEmail();
            // Mock 객체로 userRepository 설정
            when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(users));
            // Mock 객체로 existUser 설정
            Users existUser = new Users("admin@example.com", "AdminUser", "adminPassword", UserRoleEnum.ADMIN, "admin.jpg");

            // when
            Throwable exception = assertThrows(CustomException.class, () -> profileService.getProfile(users));
            // then
            assertEquals(ErrorCode.PROFILE_NOT_EXIST, ((CustomException) exception).getErrorCode());
        }
    }
}