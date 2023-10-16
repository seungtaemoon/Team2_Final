package com.sparta.team2project.users.social.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoDto {
    private Long id;
    private String email;
    private String nickName;
    private String profileImg;


    public KakaoUserInfoDto(Long id, String nickName, String email, String profileImg) {
        this.id = id;
        this.email = email;
        this.nickName = nickName;
        this.profileImg = profileImg;
    }
}
