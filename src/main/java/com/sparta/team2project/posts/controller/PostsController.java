package com.sparta.team2project.posts.controller;

import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.security.UserDetailsImpl;
import com.sparta.team2project.posts.dto.PostResponseDto;
import com.sparta.team2project.posts.dto.TotalRequestDto;
import com.sparta.team2project.posts.dto.UpdateRequestDto;
import com.sparta.team2project.posts.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostsController {

    private final PostsService postsService;

    @PostMapping("/posts") // 게시글 생성
    public ResponseEntity<MessageResponseDto> createPost(@RequestBody TotalRequestDto totalRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(postsService.createPost(totalRequestDto,userDetails.getUsers()));
    }

    @GetMapping("/posts") // 전체 게시글 조회
    public ResponseEntity<List<PostResponseDto>> getAllPosts(){ //@AuthenticationPrincipal UserDetailsImpl userDetails 추가
        return ResponseEntity.ok(postsService.getAllPosts());
    }

//    @GetMapping("/posts/{postId}") // 단일 게시물 조회
//    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId){return ResponseEntity.ok(postsService.getPost(postId));}

    @GetMapping("/posts/rank") // 랭킹 목록 조회
    public ResponseEntity<List<PostResponseDto>> getRankPosts(){return ResponseEntity.ok(postsService.getRankPosts());}

    @GetMapping("/posts/like/{postId}") // 좋아요 기능
    public ResponseEntity<MessageResponseDto> like(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(postsService.like(postId,userDetails.getUsers()));
    }

    @PutMapping("/posts/{postId}") // 게시글 수정
    public ResponseEntity<MessageResponseDto> updatePost(@PathVariable Long postId, @RequestBody UpdateRequestDto updateRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(postsService.updatePost(postId,updateRequestDto,userDetails.getUsers()));
    }

    @DeleteMapping("/posts/{postId}") // 게시글 수정
    public ResponseEntity<MessageResponseDto> deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(postsService.deletePost(postId,userDetails.getUsers()));
    }
}
