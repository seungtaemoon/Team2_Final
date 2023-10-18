package com.sparta.team2project.schedules.controller;


import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.security.UserDetailsImpl;
import com.sparta.team2project.schedules.dto.CreateSchedulesRequestDto;
import com.sparta.team2project.schedules.dto.SchedulesRequestDto;
import com.sparta.team2project.schedules.dto.SchedulesResponseDto;
import com.sparta.team2project.schedules.service.SchedulesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SchedulesController {
    private final SchedulesService schedulesService;

    // 세부일정 생성
    @PostMapping("/tripDate/{tripDateId}/schedules")
    public MessageResponseDto createSchedules(@PathVariable("tripDateId") Long tripDateId,
                                                              @RequestBody CreateSchedulesRequestDto requestDtoList,
                                                              @AuthenticationPrincipal UserDetailsImpl userDetails){
        return schedulesService.createSchedules(tripDateId,requestDtoList,userDetails.getUsers());
    }
    // 세부일정 조회
    @GetMapping("/schedules/{schedulesId}")
    public SchedulesResponseDto getSchedules(@PathVariable("schedulesId") Long schedulesId
    ) {
        return schedulesService.getSchedules(schedulesId);
    }

    // 세부일정 수정
    @PutMapping("/schedules/{schedulesId}")
    public SchedulesResponseDto updateSchedules(@PathVariable("schedulesId") Long schedulesId,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                @RequestBody SchedulesRequestDto schedulesRequestDto
    ) {
        return schedulesService.updateSchedules(schedulesId, userDetails.getUsers(), schedulesRequestDto);
    }

    // 세부일정 삭제
    @DeleteMapping("/schedules/{schedulesId}")
    public MessageResponseDto deleteSchedules(@PathVariable("schedulesId") Long schedulesId,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        return schedulesService.deleteSchedules(schedulesId, userDetails.getUsers());
    }

}
