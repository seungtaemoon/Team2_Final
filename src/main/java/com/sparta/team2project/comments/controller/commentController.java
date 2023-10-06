package com.sparta.team2project.comments.controller;

import com.sparta.team2project.comments.dto.CommentsRequestDto;
import com.sparta.team2project.comments.dto.CommentsResponseDto;
import com.sparta.team2project.comments.service.CommentsService;
import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class commentController {
    private final CommentsService commentsService;

    // 댓글 생성
    @PostMapping("/post/{postid}/comments/{commentsid}")
    public MessageResponseDto commentsCreate(@PathVariable("postid") Long postid,
                                             @PathVariable("commentsid") Long commentsid,
                                             @RequestBody CommentsRequestDto request,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentsService.commentsCreate(postid, commentsid, request, userDetails);
    }

    // 댓글 조회
    @GetMapping("/post/{postid}/comments")
    public List<CommentsResponseDto> commentsList(@PathVariable("postid") Long postid,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentsService.commentsList(postid, userDetails);
    }

    // 댓글 수정
    @PutMapping("/post/{postid}/comments/{commentsid}")
    public MessageResponseDto commentsUpdate(@PathVariable("postid") Long postid,
                                             @PathVariable("commentsid") Long commentsid,
                                             @RequestBody CommentsRequestDto request,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentsService.commentsUpdate(postid, commentsid, request, userDetails);
    }

    // 댓글 삭제
    @DeleteMapping("/post/{postid}/comments/{commentsid}")
    public MessageResponseDto commentsDelete(@PathVariable("postid") Long postid,
                                             @PathVariable("commentsid") Long commentsid,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentsService.commentsDelete(postid, commentsid, userDetails);
    }
}
