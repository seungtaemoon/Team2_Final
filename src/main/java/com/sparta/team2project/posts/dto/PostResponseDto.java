package com.sparta.team2project.posts.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.team2project.posts.entity.Posts;
import com.sparta.team2project.users.Users;
import lombok.Getter;
import lombok.RequiredArgsConstructor;



import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponseDto {

    private final Long postId;
    private final String title;
    private final String nickName;
    private final int likeNum;
    private final LocalDateTime createdAt;
    private List<PostDetailResponseDto> commentList;

    public PostResponseDto(Posts posts, Users users){
        this.postId = posts.getId();
        this.title = posts.getTitle();
        this.nickName = users.getNickName();
        this.likeNum = posts.getLikeNum();
        this.createdAt = posts.getCreatedAt();
    }

    public PostResponseDto(Posts posts, Users users,List<PostDetailResponseDto> dtoList){
        this.postId = posts.getId();
        this.title = posts.getTitle();
        this.nickName = users.getNickName();
        this.likeNum = posts.getLikeNum();
        this.createdAt = posts.getCreatedAt();
        this.commentList = dtoList;
    }


}
