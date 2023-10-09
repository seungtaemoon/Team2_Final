package com.sparta.team2project.comments.controller;

import com.sparta.team2project.comments.dto.CommentsRequestDto;
import com.sparta.team2project.comments.dto.CommentsResponseDto;
import com.sparta.team2project.comments.service.CommentsService;
import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {
    private final CommentsService commentsService;

    // 댓글 생성
//    @PostMapping("/post/{postid}/comments")
//    public ResponseEntity<CommentsResponseDto> commentsCreate(@PathVariable("postid") Long postid,
//                                                             @RequestBody CommentsRequestDto requestDto,
//                                                             @AuthenticationPrincipal UserDetailsImpl userDetails,
//                                                             HttpServletRequest req) {
//        return ResponseEntity.ok(commentsService.commentsCreate(postid, requestDto, userDetails, req));
//    }

    // 댓글 조회
//    @GetMapping("/post/{postid}/comments")
//    public ResponseEntity<List<CommentsResponseDto>> commentsList(@PathVariable("postid") Long postid,
//                                                                  @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return ResponseEntity.ok(commentsService.commentsList(postid, userDetails));
//    }

    // 댓글 수정
//    @PutMapping("/post/{postid}/comments/{commentsid}")
//    public ResponseEntity<CommentsResponseDto> commentsUpdate(@PathVariable("postid") Long postid,
//                                             @PathVariable("commentsid") Long commentsid,
//                                             @RequestBody CommentsRequestDto request,
//                                             @AuthenticationPrincipal UserDetailsImpl userDetails,
//                                             HttpServletRequest req) {
//       return ResponseEntity.ok(commentsService.commentsUpdate(postid, commentsid, request, userDetails, req));
//    }

    // 댓글 삭제
//    @DeleteMapping("/post/{postid}/comments/{commentsid}")
//    public ResponseEntity<MessageResponseDto> commentsDelete(@PathVariable("postid") Long postid,
//                                             @PathVariable("commentsid") Long commentsid,
//                                             @AuthenticationPrincipal UserDetailsImpl userDetails,
//                                             HttpServletRequest req) {
//        return ResponseEntity.ok(commentsService.commentsDelete(postid, commentsid, userDetails, req));
//    }
}
