package com.sparta.team2project.comments.service;

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
import com.sparta.team2project.replies.dto.RepliesResponseDto;
import com.sparta.team2project.replies.entity.Replies;
import com.sparta.team2project.users.entity.Users;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentsService {
    private final CommentsRepository commentsRepository;
    private final PostsRepository postsRepository;

    // 댓글 생성
    public CommentsResponseDto commentsCreate(Long postId,
                                              CommentsRequestDto requestDto,
                                              Users users) {

        Posts posts = postsRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.POST_NOT_EXIST)); // 존재하지 않는 게시글입니다

        Comments comments = new Comments(requestDto);
        posts.addComments(comments);
        MessageResponseDto messageResponseDto = new MessageResponseDto(
                "댓글을 작성하였습니다", 200
        );

        return new CommentsResponseDto(commentsRepository.save(comments));
    }

    // 댓글 조회
    public List<CommentsResponseDto> commentsList(Long postId) {
        List<Comments> commentsList = commentsRepository.findByPostIdOrderByCreatedAtDesc(postId);

        List<CommentsResponseDto> commentsResponseDtoList = new ArrayList<>();

        for (Comments comments : commentsList) {
            List<RepliesResponseDto> repliesList = new ArrayList<>();
            for (Replies replies : comments.getRepliesList()) {
                repliesList.add(new RepliesResponseDto(replies));
            }
            commentsResponseDtoList.add(new CommentsResponseDto(comments, repliesList));
        }
        return commentsResponseDtoList;
    }

    // 댓글 수정
    @Transactional
    public CommentsResponseDto commentsUpdate(Long postId,
                                              Long commentId,
                                              CommentsRequestDto request,
                                              Users users) {

        Posts posts = postsRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.POST_NOT_EXIST)); // 존재하지 않는 게시글입니다

        Comments comments = findById(commentId);
        if (users.getUserRole() == UserRoleEnum.ADMIN) {
            comments.update(request);
            MessageResponseDto messageResponseDto = new MessageResponseDto(
                    "댓글을 수정하였습니다", 200
            );

            List<RepliesResponseDto> repliesList = new ArrayList<>();
            for (Replies replies : comments.getRepliesList()) {
                repliesList.add(new RepliesResponseDto(replies));
            }
            return new CommentsResponseDto(comments, repliesList);

        } else {
            if (users.getNickName().equals(comments.getNickname())) {
                comments.update(request);
                MessageResponseDto messageResponseDto = new MessageResponseDto(
                        "댓글을 수정하였습니다", 200
                );

                List<RepliesResponseDto> repliesList = new ArrayList<>();
                for (Replies replies : comments.getRepliesList()) {
                    repliesList.add(new RepliesResponseDto(replies));
                }
                return new CommentsResponseDto(comments, repliesList);

            } else {
                throw new CustomException(ErrorCode.NOT_ALLOWED); // 권한이 없습니다
            }
        }

    }

    // 댓글 삭제
    public MessageResponseDto commentsDelete(Long postId,
                                             Long commentId,
                                             Users users) {

        Posts posts = postsRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.POST_NOT_EXIST)); // 존재하지 않는 게시글입니다

        Comments comments = findById(commentId);
        if (users.getUserRole() == UserRoleEnum.ADMIN) {
            commentsRepository.delete(comments);
            return new MessageResponseDto("댓글을 삭제하였습니다", 200);
        } else {
            if (users.getNickName().equals(comments.getNickname())) {
                commentsRepository.delete(comments);
                MessageResponseDto messageResponseDto = new MessageResponseDto(
                        "댓글을 삭제하였습니다", 200
                );
                return new MessageResponseDto("댓글을 삭제하였습니다", 200);
            } else {
                throw new CustomException(ErrorCode.NOT_ALLOWED); // 권한이 없습니다
            }
        }
    }

    private Comments findById(Long id) {
        return commentsRepository.findById(id).orElseThrow(
                () -> new NullPointerException("유효하지 않은 댓글입니다")
        );
    }
}


