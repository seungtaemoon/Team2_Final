package com.sparta.team2project.days.controller;

import com.sparta.team2project.commons.security.UserDetailsImpl;
import com.sparta.team2project.days.service.DaysService;
import com.sparta.team2project.posts.dto.DayRequestDto;
import com.sparta.team2project.posts.dto.DayResponseDto;
import com.sparta.team2project.posts.dto.DaysOnlyRequestDto;
import com.sparta.team2project.schedules.dto.SchedulesRequestDto;
import com.sparta.team2project.schedules.dto.SchedulesResponseDto;
import com.sparta.team2project.schedules.service.SchedulesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DaysController {
    private final DaysService daysService;

    // ChosenDate와 스케줄 전체를 수정하는 메서드
    @PutMapping("/days/{daysId}")
    public DayResponseDto updateDays(@PathVariable("daysId") Long daysId,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails,
                                         @RequestBody DaysOnlyRequestDto daysOnlyRequestDto
                                         ) {
        return daysService.updateDays(daysId, userDetails.getUsers(), daysOnlyRequestDto);
    }
}
