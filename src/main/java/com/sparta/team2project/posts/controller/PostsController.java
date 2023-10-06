package com.sparta.team2project.posts.controller;

import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.posts.dto.TotalRequestDto;
import com.sparta.team2project.posts.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostsController {

    private final PostsService postsService;

    @PostMapping("/posts")
    public ResponseEntity<MessageResponseDto> createPost(@RequestBody TotalRequestDto totalRequestDto){ //@AuthenticationPrincipal UserDetailsImpl userDetails 추가
        return ResponseEntity.ok(postsService.createPost(totalRequestDto));
    }
}
