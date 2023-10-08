package com.sparta.team2project.profile.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.team2project.profile.Profile;
import lombok.Getter;

@Getter
public class ProfileResponseDto {
    private String email; // 고정 값(수정 불가)
    @JsonIgnore
//    private String password; //숨겨야하는 값
    private String nickName;
    private String profileImg;

    public ProfileResponseDto(Profile profile) {
        this.email = profile.getUsers().getEmail();
//        this.password = profile.getUsers().getPassword();
        this.nickName = profile.getUsers().getNickName();
        this.profileImg = profile.getUsers().getProfileImg();
    }
}
