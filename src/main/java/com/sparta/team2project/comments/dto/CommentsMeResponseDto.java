package com.sparta.team2project.comments.dto;

import com.sparta.team2project.comments.entity.Comments;
import lombok.Getter;

@Getter
public class CommentsMeResponseDto {
        private Long commnetId;
        private String contents;
        private String nickname;
        private String title;

        public CommentsMeResponseDto(Comments comments, String postTitle, String users) {
            this.commnetId = comments.getId();
            this.contents = comments.getContents();
            this.nickname = users;
            this.title = postTitle;
        }
    }

