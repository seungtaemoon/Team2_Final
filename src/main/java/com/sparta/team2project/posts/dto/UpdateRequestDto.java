package com.sparta.team2project.posts.dto;

import com.sparta.team2project.posts.entity.PostCategory;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UpdateRequestDto {
    private String title;
    private String contents;
    private PostCategory postCategory;
//    private LocalDate startDate;
//    private LocalDate endDate;
}
