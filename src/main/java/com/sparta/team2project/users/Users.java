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
    private String nickName = "익명"; // 기본값 설정

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum userRole = UserRoleEnum.USER; // 기본값 설정

    @Column(nullable = false)
    private String profileImg = "https://blog.kakaocdn.net/dn/bEuUJE/btsxkC03HfA/KfYkjsCIVSMk3rxSnUiO9K/img.png"; // 기본값 설정

// 연관관계
//    @OneToMany(mappedBy = "users")
//    private Posts posts;

//// 회원 가입
//    public Users(String email, String password, UserRoleEnum userRole) {
//        this.email = email;
//        this.password = password;
//        this.userRole = userRole;
//    }

    // 회원 가입 후 프로필 수정?
    public Users(String email, String nickName, String password, UserRoleEnum userRole, String profileImg) {
        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.userRole = userRole;
        this.profileImg = profileImg;
    }


    public Users(String email, String password, UserRoleEnum userRole) {
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }
}
