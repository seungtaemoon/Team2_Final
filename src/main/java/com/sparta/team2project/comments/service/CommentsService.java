package com.sparta.team2project.comments.service;

import com.sparta.team2project.comments.dto.CommentsRequestDto;
import com.sparta.team2project.comments.dto.CommentsResponseDto;
import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentsService {
    private final CommentsRepository commentsRepository;
    private final PostRepository postRepository;

    public MessageResponseDto commentsCreate(Long postid, Long commentsid, CommentsRequestDto request, UserDetailsImpl userDetails) {
    }

    public List<CommentsResponseDto> commentsList(Long postid, UserDetailsImpl userDetails) {
    }

    public MessageResponseDto commentsUpdate(Long postid, Long commentsid, CommentsRequestDto request, UserDetailsImpl userDetails) {
    }

    public MessageResponseDto commentsDelete(Long postid, Long commentsid, UserDetailsImpl userDetails) {
    }
}
