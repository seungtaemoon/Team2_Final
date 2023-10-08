package com.sparta.team2project.profile;

import com.sparta.team2project.profile.dto.ProfileRequestDto;
import com.sparta.team2project.users.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "users_id")
    private Users users;

    private String nickName;
    private String profileImg;
    private String password;


    public Profile(Users users, String password, String nickName, String profileImg) {
        this.users = users;
        this.nickName = nickName;
        this.profileImg = profileImg;
        this.password = password;    }

    // 프로필 정보 업데이트 메서드
    public void updateProfile(ProfileRequestDto requestDto) {
        this.nickName = requestDto.getUpdateNickName();
        this.profileImg = requestDto.getUpdateProfileImg();
    }

    public void updatePassword(ProfileRequestDto requestDto, PasswordEncoder passwordEncoder) {
        // 비밀번호를 인코딩하여 저장
        this.password = passwordEncoder.encode(requestDto.getUpdatePassword());
    }
}

