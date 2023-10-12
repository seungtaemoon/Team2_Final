package com.sparta.team2project.comments.controller;

import com.sparta.team2project.comments.dto.CommentsMeResponseDto;
import com.sparta.team2project.comments.dto.CommentsRequestDto;
import com.sparta.team2project.comments.dto.CommentsResponseDto;
import com.sparta.team2project.comments.service.CommentsService;
import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<Page<CommentsResponseDto>> commentsList(@PathVariable("postId") Long postId,
                                                                  @RequestParam("page") int page) {
        return ResponseEntity.ok(commentsService.commentsList(postId, page-1));
    }

    // 마이페이지에서 내가 쓴 댓글 조회
    @GetMapping("/posts/{postId}/commentsme")
    public ResponseEntity<List<CommentsMeResponseDto>> commentsMeList(@PathVariable("postId") Long postId,
                                                                      @AuthenticationPrincipal UserDetailsImpl userDetails ) {
        return ResponseEntity.ok(commentsService.commentsMeList(postId, userDetails.getUsers()));
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
