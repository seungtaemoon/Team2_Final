package com.sparta.team2project.posts.dto;

import com.sparta.team2project.posts.entity.Posts;
import com.sparta.team2project.users.Users;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class PostResponseDto {

    private final Long postid;
    private final String title;
    private final String nickName;
    private final int likeNum;
    private final LocalDateTime createdAt;

    public PostResponseDto(Posts posts, Users users){
        this.postid = posts.getId();
        this.title = posts.getTitle();
        this.nickName = users.getNickName();
        this.likeNum = posts.getLikeNum();
        this.createdAt = posts.getCreatedAt();
    }


}
