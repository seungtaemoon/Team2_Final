package com.sparta.team2project.comments.service;

import com.sparta.team2project.comments.dto.CommentsRequestDto;
import com.sparta.team2project.comments.dto.CommentsResponseDto;
import com.sparta.team2project.comments.repository.CommentsRepository;
import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.jwtutil.JwtUtil;
import com.sparta.team2project.commons.security.UserDetailsImpl;
import com.sparta.team2project.postrs.entity.Posts;
import com.sparta.team2project.postrs.repository.PostsRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Comments;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentsService {
    private final CommentsRepository commentsRepository;
    private final PostsRepository postsRepository;
    private final JwtUtil jwtUtil;




    private Comments findCommentsById(Long id) {
        return commentsRepository.findById(id).orElseThrow(
                () -> new NullPointerException("유효하지 않은 댓글입니다")
        );
    }

    private Posts findPostById(Long id) {
        return postsRepository.findById(id).orElseThrow(
                () -> new NullPointerException("유효하지 않은 댓글입니다")
        );
    }

    private Claims userInfo(HttpServletRequest req) {
        String givenToken = jwtUtil.getTokenFromRequest(req);
        givenToken = jwtUtil.substringToken(givenToken);
        if (!jwtUtil.validateToken(givenToken)) throw new IllegalArgumentException("Invalid User Credentials");
        return jwtUtil.getUserInfoFromToken(givenToken);
    }
}
