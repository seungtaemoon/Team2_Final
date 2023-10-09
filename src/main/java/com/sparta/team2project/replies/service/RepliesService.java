package com.sparta.team2project.replies.service;

import com.sparta.team2project.comments.entity.Comments;
import com.sparta.team2project.comments.repository.CommentsRepository;
import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.exceptionhandler.CustomException;
import com.sparta.team2project.commons.exceptionhandler.ErrorCode;
import com.sparta.team2project.replies.dto.RepliesRequestDto;
import com.sparta.team2project.replies.dto.RepliesResponseDto;
import com.sparta.team2project.replies.entity.Replies;
import com.sparta.team2project.replies.repository.RepliesRepository;
import com.sparta.team2project.users.UserRoleEnum;
import com.sparta.team2project.users.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RepliesService {
    private final RepliesRepository repliesRepository;
    private final CommentsRepository commentsRepository;


    // 대댓글 생성
    public MessageResponseDto repliesCreate(Long commentId,
                                            RepliesRequestDto requestDto,
                                            Users users) {

        Comments comments = commentsRepository.findById(commentId).orElseThrow(
                () -> new CustomException(ErrorCode.COMMENTS_NOT_EXIST)); // 존재하지 않는 댓글입니다

        Replies replies = new Replies(requestDto, users, comments);
        repliesRepository.save(replies);

        return new MessageResponseDto ("대댓글을 작성하였습니다", 200);
    }

    // 대댓글 조회
    public List<RepliesResponseDto> repliesList(Long commentId) {
        List<Replies> repliesList = repliesRepository.findByComments_IdOrderByCreatedAtDesc(commentId);
        if (repliesList.isEmpty()) {
            throw new CustomException(ErrorCode.COMMENTS_NOT_EXIST); // 존재하지 않는 댓글입니다
        }

        List<RepliesResponseDto> repliesResponseDtoList = new ArrayList<>();

        for (Replies replies : repliesList) {
            repliesResponseDtoList.add(new RepliesResponseDto(replies, replies.getNickname()));
        }
        return repliesResponseDtoList;
    }

    @Transactional
    // 대댓글 수정
    public MessageResponseDto repliesUpdate( Long repliesId,
                                             RepliesRequestDto request,
                                             Users users) {

        Replies replies = findById(repliesId);
        if (users.getUserRole() == UserRoleEnum.ADMIN) {
            replies.update(request, users);
            return new MessageResponseDto("관리자가 대댓글을 수정하였습니다", 200);
        } else if (users.getNickName().equals(replies.getNickname())) {
                replies.update(request, users);
                return new MessageResponseDto("대댓글을 수정하였습니다", 200);
            } else {
                throw new CustomException(ErrorCode.NOT_ALLOWED); // 권한이 없습니다
        }
    }


    // 대댓글 삭제
    public MessageResponseDto repliesDelete(Long repliesId,
                                            Users users) {

        Replies replies = findById(repliesId);
        if (users.getUserRole() == UserRoleEnum.ADMIN) {
            repliesRepository.delete(replies);
            return new MessageResponseDto("관리자가 대댓글을 삭제하였습니다", 200);
        } else if (users.getNickName().equals(replies.getNickname())) {
                repliesRepository.delete(replies);
                return new MessageResponseDto("대댓글을 삭제하였습니다", 200);
            } else {
                throw new CustomException(ErrorCode.NOT_ALLOWED); // 권한이 없습니다
        }
    }


    private Replies findById(Long id) {
        return repliesRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.REPLIES_NOT_EXIST)); // 존재하지 않는 대댓글입니다
    }
}
