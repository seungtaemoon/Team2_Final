package com.sparta.team2project.comments.controller;

import com.sparta.team2project.comments.dto.CommentsRequestDto;
import com.sparta.team2project.comments.dto.CommentsResponseDto;
import com.sparta.team2project.comments.service.CommentsService;
import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.security.UserDetailsImpl;
import com.sparta.team2project.users.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentsController {
    private final CommentsService commentsService;

    // 댓글 생성
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<MessageResponseDto> commentsCreate(@PathVariable("postId") Long postId,
                                                              @RequestBody CommentsRequestDto requestDto,
                                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(commentsService.commentsCreate(postId, requestDto, userDetails.getUsers()));
    }

    // 댓글 조회
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentsResponseDto>> commentsList(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(commentsService.commentsList(postId));
    }

    // 댓글 수정
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<MessageResponseDto> commentsUpdate( @PathVariable("commentId") Long commentId,
                                                              @RequestBody CommentsRequestDto request,
                                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
       return ResponseEntity.ok(commentsService.commentsUpdate( commentId, request, userDetails.getUsers()));
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<MessageResponseDto> commentsDelete(@PathVariable("commentId") Long commentId,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(commentsService.commentsDelete(commentId, userDetails.getUsers()));
    }
}
