package com.sparta.team2project.replies.dto;

import com.sparta.team2project.replies.entity.Replies;
import lombok.Getter;

@Getter
public class RepliesMeResponseDto {
    private Long repliesId;
    private String contents;
    private String nickname;
    private String title;

    public RepliesMeResponseDto(Replies replies, String postTitle, String users) {
        this.repliesId = replies.getId();
        this.contents = replies.getContents();
        this.nickname = users;
        this.title = postTitle;
    }
}
