package com.sparta.team2project.users;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickName;
//    private String nickName = "익명"; // 기본값 설정

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum userRole; // 기본값 설정

    @Column(nullable = false)
    private String profileImg;
//    private String profileImg = "https://blog.kakaocdn.net/dn/bEuUJE/btsxkC03HfA/KfYkjsCIVSMk3rxSnUiO9K/img.png"; // 기본값 설정

// 연관관계
//    @OneToMany(mappedBy = "users")
//    private Posts posts;

    // 회원 가입
    public Users(String email, String nickName, String password, UserRoleEnum userRole, String profileImg) {
        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.userRole = userRole;
        this.profileImg = profileImg;
    }

    public Users(String email, String nickName, String password, String profileImg) {
        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.profileImg = profileImg;
    }

//    // 회원 정보 수정
//    public void updateProfile(String newNickName, String newProfileImg) {
//        // 새로운 닉네임이 제공된 경우에만 업데이트
//        if (newNickName != null) {
//            this.nickName = newNickName;
//        }
//        if (newProfileImg != null) {
//            this.profileImg = newProfileImg;
//        }
//    }
}
