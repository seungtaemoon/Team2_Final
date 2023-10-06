package com.sparta.team2project.posts.dto;

import com.sparta.team2project.schedules.entity.Schedules;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class DayRequestDto {

    private LocalDate chosenDate;
    private List<Schedules> scheduleList;
}
