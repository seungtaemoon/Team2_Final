package com.sparta.team2project.replies.controller;

import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.security.UserDetailsImpl;
import com.sparta.team2project.posts.dto.PostsRequestDto;
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
    @PostMapping("/post/{postId}/comments/{commentId}/replies")
    public ResponseEntity<RepliesResponseDto> repliesCreate(@PathVariable("postId") Long postId,
                                                            @PathVariable("commentId") Long commentId,
                                                            @RequestBody RepliesRequestDto requestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(repliesService.repliesCreate(postId, commentId, requestDto, userDetails.getUsers()));
    }

    // 대댓글 조회
    @GetMapping("/post/{postId}/comments/{commentId}/replies")
    public ResponseEntity<List<RepliesResponseDto>> repliesList(@PathVariable("postId") Long postId,
                                                                 @PathVariable("commentId") Long commentId) {
        return ResponseEntity.ok(repliesService.repliesList(postId, commentId));
    }

    // 대댓글 수정
    @PutMapping("/post/{postId}/comments/{commentId}/replies/{repliesId}")
    public ResponseEntity<RepliesResponseDto> repliesUpdate(@PathVariable("postId") Long postId,
                                                             @PathVariable("commentId") Long commentId,
                                                             @PathVariable("repliesId") Long repliesId,
                                                             @RequestBody RepliesRequestDto request,
                                                             @RequestBody PostsRequestDto requestDto,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(repliesService.repliesUpdate(postId, commentId, repliesId, request, requestDto, userDetails.getUsers()));
    }

    // 대댓글 삭제
    @DeleteMapping("/post/{postId}/comments/{commentId}/replies/{repliesId}")
    public ResponseEntity<MessageResponseDto> repliesDelete(@PathVariable("postId") Long postId,
                                                            @PathVariable("commentId") Long commentId,
                                                            @PathVariable("repliesId") Long repliesId,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(repliesService.repliesDelete(postId, commentId, repliesId, userDetails.getUsers()));
    }
}
