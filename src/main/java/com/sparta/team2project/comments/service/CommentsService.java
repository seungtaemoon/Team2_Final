package com.sparta.team2project.comments.service;

import com.sparta.team2project.comments.dto.CommentsMeResponseDto;
import com.sparta.team2project.comments.dto.CommentsRequestDto;
import com.sparta.team2project.comments.dto.CommentsResponseDto;
import com.sparta.team2project.comments.entity.Comments;
import com.sparta.team2project.comments.repository.CommentsRepository;
import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.entity.UserRoleEnum;
import com.sparta.team2project.commons.exceptionhandler.CustomException;
import com.sparta.team2project.commons.exceptionhandler.ErrorCode;
import com.sparta.team2project.posts.entity.Posts;
import com.sparta.team2project.posts.repository.PostsRepository;
import com.sparta.team2project.users.Users;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentsService {
    private final CommentsRepository commentsRepository;
    private final PostsRepository postsRepository;

    // 댓글 생성
    public MessageResponseDto commentsCreate(Long postId,
                                              CommentsRequestDto requestDto,
                                              Users users) {

        Posts posts = postsRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.POST_NOT_EXIST)); // 존재하지 않는 게시글입니다

        Comments comments = new Comments(requestDto, users, posts);
        commentsRepository.save(comments);

        return new MessageResponseDto ("댓글을 작성하였습니다", 200);
    }

    // 댓글 조회
    public Page<CommentsResponseDto> commentsList(Long postId,
                                                  int page) {
        int size = 5;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Comments> commentsPageList = commentsRepository.findByPosts_Id(postId, pageable);

        if (commentsPageList.isEmpty()) {
            throw new CustomException(ErrorCode.POST_NOT_EXIST); // 존재하지 않는 게시글입니다
        }

        Page<CommentsResponseDto> commentsResponseDtoPage = commentsPageList.map(comments -> new CommentsResponseDto(comments, comments.getNickname()));

        return commentsResponseDtoPage;
    }

   // 마이페이지에서 내가 쓴 댓글 조회
    public List<CommentsMeResponseDto> commentsMeList (Long postId,
                                                       Users users) {
        List<Comments> commentsMeList = commentsRepository.findByPosts_IdOrderByCreatedAtDesc(postId);
        if (commentsMeList.isEmpty()) {
            throw new CustomException(ErrorCode.POST_NOT_EXIST); // 존재하지 않는 게시글입니다
        }

        List<CommentsMeResponseDto> commentsMeResponseDtoList = new ArrayList<>();
        for (Comments comments : commentsMeList) {

            if (users.getEmail().equals(comments.getEmail())) {
                    commentsMeResponseDtoList.add(new CommentsMeResponseDto(comments, comments.getPosts().getTitle(), comments.getNickname()));
                }
        }
        return commentsMeResponseDtoList;
    }

    // 댓글 수정
    @Transactional
    public MessageResponseDto commentsUpdate( Long commentId,
                                              CommentsRequestDto request,
                                              Users users) {
        Comments comments = findById(commentId);
        if (users.getUserRole() == UserRoleEnum.ADMIN) {
            comments.update(request, users);
            return new MessageResponseDto("관리자가 댓글을 수정하였습니다", 200);
        } else if (users.getEmail().equals(comments.getEmail())) {
                comments.update(request, users);
                return new MessageResponseDto("댓글을 수정하였습니다", 200);
            } else {
                throw new CustomException(ErrorCode.NOT_ALLOWED); // 권한이 없습니다
            }
        }

    // 댓글 삭제
    public MessageResponseDto commentsDelete(Long commentId,
                                             Users users) {

        Comments comments = findById(commentId);
        if (users.getUserRole() == UserRoleEnum.ADMIN) {
            commentsRepository.delete(comments);
                return new MessageResponseDto("관리자가 댓글을 삭제하였습니다", 200);
        } else if (users.getEmail().equals(comments.getEmail())) {
                commentsRepository.delete(comments);
                return new MessageResponseDto("댓글을 삭제하였습니다", 200);
            } else {
                throw new CustomException(ErrorCode.NOT_ALLOWED); // 권한이 없습니다
        }
    }

    private Comments findById(Long id) {
        return commentsRepository.findById(id).orElseThrow(
                () ->new CustomException(ErrorCode.COMMENTS_NOT_EXIST)); // 존재하지 않는 댓글입니다
    }
}


