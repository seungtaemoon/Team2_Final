package com.sparta.team2project.posts.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DaysOnlyRequestDto {
    private LocalDate chosenDate;
    private String subTitle;
}
