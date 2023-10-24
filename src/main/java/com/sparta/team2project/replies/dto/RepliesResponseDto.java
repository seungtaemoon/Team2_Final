package com.sparta.team2project.replies.dto;

import com.sparta.team2project.replies.entity.Replies;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RepliesResponseDto {
    private Long repliesId;
    private String contents;
    private String email;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

    public RepliesResponseDto(Replies replies, String users) {
        this.repliesId = replies.getId();
        this.contents = replies.getContents();
        this.createAt = replies.getCreatedAt();
        this.modifiedAt = replies.getModifiedAt();
        this.email = users;
    }
}

