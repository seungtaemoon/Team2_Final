package com.sparta.team2project.tripdate.controller;

import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.security.UserDetailsImpl;
import com.sparta.team2project.tripdate.service.TripDateService;
import com.sparta.team2project.posts.dto.TripDateResponseDto;
import com.sparta.team2project.posts.dto.TripDateOnlyRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TripDateController {
    private final TripDateService tripDateService;

    // TripDate 조회 메서드
    @GetMapping("/tripDate/{tripDateId}")
    public TripDateResponseDto getTripDate(@PathVariable("tripDateId") Long tripDateId){
        return tripDateService.getTripDate(tripDateId);
    }

    // TripDate 전체 조회 메서드
    @GetMapping("/posts/{postId}/tripDate")
    public List<TripDateResponseDto> getTripDateAll(@PathVariable("postId") Long postId){
        return tripDateService.getTripDateAll(postId);
    }

    // TripDate를 수정하는 메서드
    @PutMapping("/tripDate/{tripDateId}")
    public TripDateResponseDto updateTripDate(@PathVariable("tripDateId") Long tripDateId,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @RequestBody TripDateOnlyRequestDto tripDateOnlyRequestDto
                                         ) {
        return tripDateService.updateTripDate(tripDateId, userDetails.getUsers(), tripDateOnlyRequestDto);
    }

    // TripDate를 삭제하는 메서드
    @DeleteMapping("/tripDate/{tripDateId}")
    public MessageResponseDto deleteTripDate(@PathVariable("tripDateId") Long tripDateId,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails){
        return tripDateService.deleteTripDate(tripDateId, userDetails.getUsers());
    }
}
