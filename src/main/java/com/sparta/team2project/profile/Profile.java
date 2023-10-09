package com.sparta.team2project.profile;

import com.sparta.team2project.users.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private String email;
    private String nickName;
    private String profileImg;


    public Profile(Users users) {
        this.users = users;
        this.email = users.getEmail();
        this.nickName = users.getNickName();
        this.profileImg = users.getProfileImg();
    }
}

