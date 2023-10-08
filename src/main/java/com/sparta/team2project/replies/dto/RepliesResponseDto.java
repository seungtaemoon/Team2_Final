package com.sparta.team2project.replies.dto;

import com.sparta.team2project.replies.entity.Replies;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RepliesResponseDto {
    private Long id;
    private String contents;
    private String nickname;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

    public RepliesResponseDto(Replies replies) {
        this.id = replies.getId();
        this.contents = replies.getContents();
        this.nickname = replies.getNickname();
        this.createAt = replies.getCreatedAt();
        this.modifiedAt = replies.getModifiedAt();
    }
}
