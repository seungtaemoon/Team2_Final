package com.sparta.team2project.posts.dto;

import com.sparta.team2project.days.entity.Days;
import com.sparta.team2project.posts.entity.Posts;
import com.sparta.team2project.schedules.dto.SchedulesResponseDto;
import com.sparta.team2project.schedules.entity.Schedules;
import com.sparta.team2project.users.Users;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class DayResponseDto {
    private final LocalDate chosenDate;
    private final String subTitle;
    private final List<SchedulesResponseDto> schedulesList;

    public DayResponseDto(Days days){
        this.chosenDate = days.getChosenDate();
        this.subTitle = days.getSubTitle();
        this.schedulesList = schedulesToDto(days.getSchedulesList());
    }

    public List<SchedulesResponseDto> schedulesToDto(List<Schedules> schedulesListBeforeDto){
        return schedulesListBeforeDto.stream().map(SchedulesResponseDto::new).collect(Collectors.toList());
    }
}
