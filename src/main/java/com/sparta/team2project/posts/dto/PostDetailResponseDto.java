package com.sparta.team2project.posts.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PostDetailResponseDto {
    private final String comments;
    private final List<ReplyResponseDto> replyList;

    public PostDetailResponseDto(String comments, List<ReplyResponseDto> replyList) {
        this.comments = comments;
        this.replyList = replyList;
    }
}
