package com.sparta.team2project.posts.controller;

import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.security.UserDetailsImpl;
import com.sparta.team2project.posts.dto.PostResponseDto;
import com.sparta.team2project.posts.dto.TotalRequestDto;
import com.sparta.team2project.posts.dto.UpdateRequestDto;
import com.sparta.team2project.posts.service.PostsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "게시글 관련 API", description = "게시글 관련 API입니다.")
@RequestMapping("/api")
public class PostsController {

    private final PostsService postsService;

    //게시글 생성
    @Operation(summary = "게시글 생성 ", description = "게시글 생성 api 입니다.")
    @PostMapping("/posts") // 게시글 생성
    public ResponseEntity<MessageResponseDto> createPost(@RequestBody TotalRequestDto totalRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(postsService.createPost(totalRequestDto,userDetails.getUsers()));
    }
    // 단일 게시물 조회
    @Operation(summary = "게시글 상세 조회 ", description = "게시글 상세 조회 api 입니다.")
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId){return ResponseEntity.ok(postsService.getPost(postId));}


    // 게시글 전체 조회
    @Operation(summary = "게시글 전체 조회 ", description = "게시글 전체 조회 api 입니다.")
    @GetMapping("/posts")
    public ResponseEntity<Slice<PostResponseDto>> getAllPosts(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(postsService.getAllPosts(pageable));
    }

    // 게시물 검색 조회
    @Operation(summary = "게시글 검색 조회 ", description = "키워드로 게시글 검색 조회  api 입니다.")
    @GetMapping("/search")
    public ResponseEntity<List<PostResponseDto>> getKeywordPost(@RequestParam String keyword){return ResponseEntity.ok(postsService.getKeywordPosts(keyword));}

    // 랭킹 목록 조회
    @Operation(summary = " 좋아요 순 게시글 조회 ", description = "TOP3 좋아요 순 게시글 조회 api 입니다.")
    @GetMapping("/posts/rank")
    public ResponseEntity<List<PostResponseDto>> getRankPosts(){return ResponseEntity.ok(postsService.getRankPosts());}

    // 좋아요 기능
    @Operation(summary = " 좋아요 기능 ", description = "좋아요 클릭시 1 좋아요 또 누르면 1 취소하는 api 입니다.")
    @GetMapping("/posts/like/{postId}")
    public ResponseEntity<MessageResponseDto> like(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(postsService.like(postId,userDetails.getUsers()));
    }

    // 게시글 수정
    @Operation(summary = " 게시글 수정 ", description = "게시글 수정 api 입니다.")
    @PutMapping("/posts/{postId}")
    public ResponseEntity<MessageResponseDto> updatePost(@PathVariable Long postId, @RequestBody UpdateRequestDto updateRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(postsService.updatePost(postId,updateRequestDto,userDetails.getUsers()));
    }

    //게시글 삭제
    @Operation(summary = "게시글 조회 삭제", description = "게시글 삭제 api 입니다.")
    @DeleteMapping("/posts/{postId}") // 게시글 삭제
    public ResponseEntity<MessageResponseDto> deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(postsService.deletePost(postId,userDetails.getUsers()));
    }
}
