package com.sparta.team2project.posts.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ReplyResponseDto {
    private final String contents;
    private final String nickName;

    private final LocalDateTime createTime; // 커밋전 삭제

    public ReplyResponseDto(String contents, String nickName, LocalDateTime create) { // create 삭제
        this.contents = contents;
        this.nickName = nickName;

        this.createTime = create; // 커밋전 삭제
    }
}
