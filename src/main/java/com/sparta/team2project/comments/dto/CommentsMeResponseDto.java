package com.sparta.team2project.comments.dto;

import com.sparta.team2project.comments.entity.Comments;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentsMeResponseDto {
        private Long commnetId;
        private String contents;
        private String title;
        private LocalDateTime createAt;

        public CommentsMeResponseDto(Comments comments, String postTitle) {
            this.commnetId = comments.getId();
            this.contents = comments.getContents();
            this.title = postTitle;
            this.createAt = comments.getCreatedAt();
        }
    }

