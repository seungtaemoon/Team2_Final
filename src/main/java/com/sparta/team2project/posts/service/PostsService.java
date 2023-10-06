package com.sparta.team2project.posts.service;

import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.exceptionhandler.CustomException;
import com.sparta.team2project.commons.exceptionhandler.ErrorCode;
import com.sparta.team2project.posts.dto.TotalRequestDto;
import com.sparta.team2project.posts.entity.Posts;
import com.sparta.team2project.posts.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostsService {
    private final PostsRepository postsRepository;
    //private final UserssRepository usersRepository;
    public MessageResponseDto createPost(TotalRequestDto totalRequestDto) {
//        Users existUser = checkUser(users); // 유저 확인
//
//        if (!existUser.getRole().equals(UserRoleEnum.ADMIN)&&! existUser.getEmail().equals(users.getEmail())) {
//            throw new CustomException(ErrorCode.NOT_ALLOWED);
//        }
        Posts posts = new Posts(totalRequestDto.getLikeNum(),
                                totalRequestDto.getContents(),
                                totalRequestDto.getTitle(),
                                totalRequestDto.getPostCategory(),
                                totalRequestDto.getStartDate(),
                                totalRequestDto.getEndDate());
                                //existUser);
        postsRepository.save(posts);

        return null;
    }
//    public Users checkUser (Users users) {
//        return usersRepository.findByEmail(users.getEmail()).
//                orElseThrow(() -> new CustomException(ErrorCode.ID_NOT_MATCH));
//
//    }
}
