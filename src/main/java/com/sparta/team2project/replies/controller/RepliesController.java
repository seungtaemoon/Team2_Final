package com.sparta.team2project.replies.controller;

import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.security.UserDetailsImpl;
import com.sparta.team2project.replies.dto.RepliesRequestDto;
import com.sparta.team2project.replies.dto.RepliesResponseDto;
import com.sparta.team2project.replies.service.RepliesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RepliesController {
    private final RepliesService repliesService;

    // 대댓글 생성
    @PostMapping("comments/{commentId}/replies")
    public ResponseEntity<MessageResponseDto> repliesCreate(@PathVariable("commentId") Long commentId,
                                                            @RequestBody RepliesRequestDto requestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(repliesService.repliesCreate(commentId, requestDto, userDetails.getUsers()));
    }

    // 대댓글 조회
    @GetMapping("/comments/{commentId}/replies")
    public ResponseEntity<List<RepliesResponseDto>> repliesList(@PathVariable("commentId") Long commentId) {
        return ResponseEntity.ok(repliesService.repliesList(commentId));
    }

    // 대댓글 수정
    @PutMapping("replies/{repliesId}")
    public ResponseEntity<MessageResponseDto> repliesUpdate( @PathVariable("repliesId") Long repliesId,
                                                             @RequestBody RepliesRequestDto request,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(repliesService.repliesUpdate(repliesId, request, userDetails.getUsers()));
    }

    // 대댓글 삭제
    @DeleteMapping("replies/{repliesId}")
    public ResponseEntity<MessageResponseDto> repliesDelete(@PathVariable("repliesId") Long repliesId,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(repliesService.repliesDelete(repliesId, userDetails.getUsers()));
    }
}
