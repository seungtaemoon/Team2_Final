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
    //private final UsersRepository usersRepository;
    public MessageResponseDto createPost(TotalRequestDto totalRequestDto) { //Users users 추가하기
//        Users existUser = checkUser(users); // 유저 확인
//
//        //권한 확인
//        checkAuthority(existUser, users);

        Posts posts = new Posts(totalRequestDto.getLikeNum(),
                                totalRequestDto.getContents(),
                                totalRequestDto.getTitle(),
                                totalRequestDto.getPostCategory(),
                                totalRequestDto.getStartDate(),
                                totalRequestDto.getEndDate());
                                //existUser);
        postsRepository.save(posts);  //posts 저장

        List<DayRequestDto> dayRequestDtoList = totalRequestDto.getDayList();
        for(DayRequestDto dayRequestDto:dayRequestDtoList){
            LocalDate date = dayRequestDto.getChosenDate();
            Days days = new Days(date,posts);
            daysRepository.save(days); // days 저장

            List<Schedules>schedulesList=dayRequestDto.getScheduleList();
            for(Schedules schedules:schedulesList) {
                Schedules schedule = new Schedules(days,schedules);
                schedulesRepository.save(schedule); //스케줄 저장
            }
        }

        return new MessageResponseDto("게시글이 등록 되었습니다.", HttpServletResponse.SC_OK);
    }

    public List<PostResponseDto> getAllPost() {
        List<Posts> postsList = postsRepository.findAllByOrderByCreatedAtDesc();
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        for(Posts posts:postsList){
            postResponseDtoList.add(new PostResponseDto(posts,posts.getUsers()));
        }
        return postResponseDtoList;
    }

    public MessageResponseDto like(Long id, Users users){
         Posts posts = checkPosts(id);

//        Users existUser = checkUser(users); // 유저 확인
//
//        //권한 확인
//        checkAuthority(existUser, users);
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

//    private Users checkUser (Users users) {
//        return usersRepository.findByEmail(users.getEmail()).
//                orElseThrow(() -> new CustomException(ErrorCode.ID_NOT_MATCH));
//
//    }
//      private void checkAuthority(Users existUser,Users users){
//          if (!existUser.getRole().equals(UserRoleEnum.ADMIN)&&!existUser.getEmail().equals(users.getEmail())) {
//            throw new CustomException(ErrorCode.NOT_ALLOWED);
//        }
//      }
    private Posts checkPosts(Long id) {
        return postsRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_EXIST));
    }
}
