package com.sparta.team2project.comments.dto;

import com.sparta.team2project.comments.entity.Comments;
import com.sparta.team2project.replies.dto.RepliesResponseDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CommentsResponseDto {
    private Long id;
    private String contents;
    private String nickname;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
    private List<RepliesResponseDto> repliesList = new ArrayList<>();

    public CommentsResponseDto(Comments comments) {
        this.id = comments.getId();
        this.contents = comments.getContents();
        this.nickname = comments.getNickname();
        this.createAt = comments.getCreatedAt();
        this.modifiedAt = comments.getModifiedAt();
    }

    public CommentsResponseDto(Comments comments, List<RepliesResponseDto> repliesList) {
        this.id = comments.getId();
        this.contents = comments.getContents();
        this.nickname = comments.getNickname();
        this.createAt = comments.getCreatedAt();
        this.modifiedAt = comments.getModifiedAt();
        this.repliesList = repliesList;
    }
}
