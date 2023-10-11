package com.sparta.team2project.posts.dto;

import com.sparta.team2project.posts.entity.PostCategory;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class TotalRequestDto {
    private String title;
    private String contents;
    private PostCategory postCategory;
//    private LocalDate startDate;
//    private LocalDate endDate;
    private List<TripDateRequestDto> tripDateList;
}
