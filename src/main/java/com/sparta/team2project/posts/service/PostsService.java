package com.sparta.team2project.posts.service;

import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.exceptionhandler.CustomException;
import com.sparta.team2project.commons.exceptionhandler.ErrorCode;
import com.sparta.team2project.days.entity.Days;
import com.sparta.team2project.days.repository.DaysRepository;
import com.sparta.team2project.posts.dto.DayRequestDto;
import com.sparta.team2project.posts.dto.PostResponseDto;
import com.sparta.team2project.posts.dto.TotalRequestDto;
import com.sparta.team2project.posts.entity.Posts;
import com.sparta.team2project.posts.repository.PostsRepository;
import com.sparta.team2project.postslike.entity.PostsLike;
import com.sparta.team2project.postslike.repository.PostsLikeRepository;
import com.sparta.team2project.schedules.entity.Schedules;
import com.sparta.team2project.schedules.repository.SchedulesRepository;
import com.sparta.team2project.users.UserController;
import com.sparta.team2project.users.UserRepository;
import com.sparta.team2project.users.UserRoleEnum;
import com.sparta.team2project.users.Users;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostsService {
    private final PostsRepository postsRepository;
    private final DaysRepository daysRepository;
    private final SchedulesRepository schedulesRepository;
    private final PostsLikeRepository postsLikeRepository;
    private final UserRepository usersRepository;

    // 게시글 생성
    public MessageResponseDto createPost(TotalRequestDto totalRequestDto,Users users) {

        Users existUser = checkUser(users); // 유저 확인

        //권한 확인
        checkAuthority(existUser,users);

        Posts posts = new Posts(totalRequestDto.getContents(),
                                totalRequestDto.getTitle(),
                                totalRequestDto.getPostCategory(),
                                totalRequestDto.getStartDate(),
                                totalRequestDto.getEndDate(),
                                existUser);
        postsRepository.save(posts);  //posts 저장

        List<DayRequestDto> dayRequestDtoList = totalRequestDto.getDayList();
        for(DayRequestDto dayRequestDto:dayRequestDtoList){
            Days days = new Days(dayRequestDto,posts);
            daysRepository.save(days); // days 저장

            List<Schedules>schedulesList=new ArrayList<>();
            for(Schedules schedules:dayRequestDto.getScheduleList()) {
                schedules = new Schedules(days,schedules);
                schedulesList.add(schedules);
            }
            schedulesRepository.saveAll(schedulesList);
        }

        return new MessageResponseDto("게시글이 등록 되었습니다.", HttpServletResponse.SC_OK);
    }

    // 게시글 전체 조회
    public List<PostResponseDto> getAllPost() {

        List<Posts> postsList = postsRepository.findAllByOrderByCreatedAtDesc();
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        for(Posts posts:postsList){
            postResponseDtoList.add(new PostResponseDto(posts,posts.getUsers()));
        }
        return postResponseDtoList;
    }

    // 게시글 좋아요 및 좋아요 취소
    public MessageResponseDto like(Long id, Users users){
         Posts posts = checkPosts(id); // 게시글 확인

        Users existUser = checkUser(users); // 사용자 확인

        //권한 확인
        checkAuthority(existUser,users);
        PostsLike overlap = postsLikeRepository.findByPostsAndUsers(posts,existUser);
        if(overlap!=null){
            postsLikeRepository.delete(overlap); // 좋아요 삭제
            posts.unlike(); // 해당 게시물 좋아요 취소시키는 메서드
            return new MessageResponseDto("좋아요 취소",HttpServletResponse.SC_OK);
        }
        else{
            PostsLike postsLike = new PostsLike(posts,existUser);
            postsLikeRepository.save(postsLike); // 좋아요 저장
            posts.like(); // 해당 게시물 좋아요수 증가시키는 메서드
            return new MessageResponseDto("좋아요 확인",HttpServletResponse.SC_OK);
        }
    }

    // 사용자 확인 메서드
    private Users checkUser (Users users) {
        return usersRepository.findByEmail(users.getEmail()).
                orElseThrow(() -> new CustomException(ErrorCode.ID_NOT_MATCH));

    }
     // ADMIN 권한 및 이메일 일치여부 메서드
    private void checkAuthority(Users existUser,Users users){
        if (!existUser.getUserRole().equals(UserRoleEnum.ADMIN)&&!existUser.getEmail().equals(users.getEmail())) {throw new CustomException(ErrorCode.NOT_ALLOWED);
        }

    }

    // 게시글 확인 메서드
    private Posts checkPosts(Long id) {
        return postsRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_EXIST));
    }
}
