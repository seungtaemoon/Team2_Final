package com.sparta.team2project.replies.service;

import com.sparta.team2project.comments.entity.Comments;
import com.sparta.team2project.comments.repository.CommentsRepository;
import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.exceptionhandler.CustomException;
import com.sparta.team2project.commons.exceptionhandler.ErrorCode;
import com.sparta.team2project.posts.entity.Posts;
import com.sparta.team2project.posts.repository.PostsRepository;
import com.sparta.team2project.replies.dto.RepliesRequestDto;
import com.sparta.team2project.replies.dto.RepliesResponseDto;
import com.sparta.team2project.replies.entity.Replies;
import com.sparta.team2project.replies.repository.RepliesRepository;
import com.sparta.team2project.users.UserRoleEnum;
import com.sparta.team2project.users.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RepliesService {
    private final RepliesRepository repliesRepository;
    private final CommentsRepository commentsRepository;
    private final PostsRepository postsRepository;


    // 대댓글 생성
    public RepliesResponseDto repliesCreate(Long commentId,
                                            RepliesRequestDto requestDto,
                                            Users users) {
        Comments comments = commentsRepository.findById(commentId).orElseThrow(
                () -> new CustomException(ErrorCode.POST_NOT_EXIST)); // 존재하지 않는 게시글입니다

        Replies replies = new Replies(requestDto);
        comments.addReplies(replies);

        MessageResponseDto messageResponseDto = new MessageResponseDto(
                "대댓글을 작성하였습니다", 200
        );

        return new RepliesResponseDto(repliesRepository.save(replies));
    }

    // 대댓글 조회
    public List<RepliesResponseDto> repliesList(Long commentId) {
        return repliesRepository.findByComments_IdOrderByCreatedAtDesc(commentId).stream().map(RepliesResponseDto::new).toList();
    }

    @Transactional
    // 대댓글 수정
    public RepliesResponseDto repliesUpdate( Long commentId,
                                             Long repliesId,
                                             RepliesRequestDto request,
                                             Users users) {

        Comments comments = commentsRepository.findById(commentId).orElseThrow(
                () -> new CustomException(ErrorCode.POST_NOT_EXIST)); // 존재하지 않는 게시글입니다

        Replies replies = findById(repliesId);
        if (users.getUserRole() == UserRoleEnum.ADMIN) {
            replies.update(request);
            MessageResponseDto messageResponseDto = new MessageResponseDto(
                    "대댓글을 수정하였습니다", 200
            );
            return new RepliesResponseDto(replies);
        } else {
            if (users.getNickName().equals(comments.getNickname())) {
                replies.update(request);
                MessageResponseDto messageResponseDto = new MessageResponseDto(
                        "대댓글을 수정하였습니다", 200
                );
                return new RepliesResponseDto(replies);
            } else {
                throw new CustomException(ErrorCode.NOT_ALLOWED); // 권한이 없습니다
            }
        }
    }

    // 대댓글 삭제
    public MessageResponseDto repliesDelete(Long commentId,
                                            Long repliesId,
                                            Users users) {

        Comments comments = commentsRepository.findById(commentId).orElseThrow(
                () -> new CustomException(ErrorCode.POST_NOT_EXIST)); // 존재하지 않는 게시글입니다

        Replies replies = findById(repliesId);
        if (users.getUserRole() == UserRoleEnum.ADMIN) {
            repliesRepository.delete(replies);
            return new MessageResponseDto("대댓글을 삭제하였습니다", 200);
        } else {
            if (users.getNickName().equals(comments.getNickname())) {
                repliesRepository.delete(replies);
                MessageResponseDto messageResponseDto = new MessageResponseDto(
                        "대댓글을 삭제하였습니다", 200
                );
                return new MessageResponseDto("대댓글을 삭제하였습니다", 200);
            } else {
                throw new CustomException(ErrorCode.NOT_ALLOWED); // 권한이 없습니다
            }
        }
    }

    private Replies findById(Long id) {
        return repliesRepository.findById(id).orElseThrow(
                () -> new NullPointerException("유효하지 않은 댓글입니다")
        );
    }
}
