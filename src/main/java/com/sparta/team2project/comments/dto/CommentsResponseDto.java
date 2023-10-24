package com.sparta.team2project.comments.dto;

import com.sparta.team2project.comments.entity.Comments;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentsResponseDto {
    private Long commentId;
    private String contents;
    private String email;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;



    public CommentsResponseDto(Comments comments, String users) {
        this.commentId = comments.getId();
        this.contents = comments.getContents();
        this.createAt = comments.getCreatedAt();
        this.modifiedAt = comments.getModifiedAt();
        this.email = users;
    }
}
