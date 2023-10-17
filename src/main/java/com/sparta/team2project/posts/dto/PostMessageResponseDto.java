package com.sparta.team2project.posts.dto;

import com.sparta.team2project.posts.entity.Posts;
import lombok.Getter;

@Getter
public class PostMessageResponseDto {
    private Long id;
    private String msg;
    private int statusCode;

    public PostMessageResponseDto(String msg, int statusCode, Posts posts){
        this.id= posts.getId();
        this.msg = msg;
        this.statusCode = statusCode;
    }
}
