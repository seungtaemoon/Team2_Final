package com.sparta.team2project.posts.service;

import com.sparta.team2project.comments.entity.Comments;
import com.sparta.team2project.comments.repository.CommentsRepository;
import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.exceptionhandler.CustomException;
import com.sparta.team2project.commons.exceptionhandler.ErrorCode;
import com.sparta.team2project.days.entity.Days;
import com.sparta.team2project.days.repository.DaysRepository;
import com.sparta.team2project.posts.dto.*;
import com.sparta.team2project.posts.entity.Posts;
import com.sparta.team2project.posts.repository.PostsRepository;
import com.sparta.team2project.postslike.entity.PostsLike;
import com.sparta.team2project.postslike.repository.PostsLikeRepository;
import com.sparta.team2project.replies.entity.Replies;
import com.sparta.team2project.replies.repository.RepliesRepository;
import com.sparta.team2project.schedules.entity.Schedules;
import com.sparta.team2project.schedules.repository.SchedulesRepository;
import com.sparta.team2project.users.UserRepository;
import com.sparta.team2project.users.UserRoleEnum;
import com.sparta.team2project.users.Users;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final CommentsRepository commentsRepository;
    private final RepliesRepository repliesRepository;

    // 게시글 생성
    public MessageResponseDto createPost(TotalRequestDto totalRequestDto,Users users) {

        Users existUser = checkUser(users); // 사용자 조회

        //권한 확인
        checkAuthority(existUser,users); //(ROLE이 ADMIN이든 User든 userId(토큰만) 있으면 생성을 할 수 있으니 여기서는 제외)

        Posts posts = new Posts(totalRequestDto.getContents(),
                                totalRequestDto.getTitle(),
                                totalRequestDto.getPostCategory(),
                                totalRequestDto.getStartDate(),
                                totalRequestDto.getEndDate(),
                                existUser);
        postsRepository.save(posts);  //posts 저장

        List<DayRequestDto> dayRequestDtoList = totalRequestDto.getDaysList();
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
    public List<PostResponseDto> getAllPosts() {

        List<Posts> postsList = postsRepository.findAllByOrderByCreatedAtDesc();
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        for(Posts posts:postsList){
            postResponseDtoList.add(new PostResponseDto(posts,posts.getUsers()));
        }
        return postResponseDtoList;
    }

    // 단일 게시물 조회
    public PostResponseDto getPost(Long postId) {

        Posts posts = checkPosts(postId); // 게시물 id 조회
        List<Comments> commentsList = checkComments(posts); // 해당 게시글의 댓글 조회
        List<PostDetailResponseDto>totalCommentRepliesDto = new ArrayList<>(); // response로 반환할 객체 리스트

        for(Comments comments:commentsList){
            List<ReplyResponseDto> repliesAboutList = new ArrayList<>(); // commentsID별 replies 객체에 대한 필드 담을 리스트

            List<Replies> repliesList = repliesRepository.findAllByCommentsOrderByCreatedAtDesc(comments);// 해당 댓글 관련 replies 객체가 들어있는 리스트
            for(Replies replies:repliesList){ // replies 객체 하니씩 빼옴
                String contents = replies.getContents();
                String nickName = replies.getNickname();

                LocalDateTime create = replies.getCreatedAt();//(커밋전 삭제)
                ReplyResponseDto replyResponseDto = new ReplyResponseDto(contents,nickName,create);//(커밋전 create 삭제)
                repliesAboutList.add(replyResponseDto);
            }
            PostDetailResponseDto dto = new PostDetailResponseDto(comments.getContents(),repliesAboutList);
            totalCommentRepliesDto.add(dto);
        }
        return new PostResponseDto(posts,posts.getUsers(),totalCommentRepliesDto);
    }

    // 랭킹 목록 조회(상위 3개)
    public List<PostResponseDto> getRankPosts() {

        // 상위 3개 게시물 가져오기 (좋아요 수 겹칠 시 createAt 내림차순으로 정렬)
        List<Posts> postsList = postsRepository.findFirst3ByOrderByLikeNumDescCreatedAtDesc();
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        for(Posts posts:postsList){
            postResponseDtoList.add(new PostResponseDto(posts,posts.getUsers()));
        }
        return postResponseDtoList;
    }

    // 게시글 좋아요 및 좋아요 취소
    public MessageResponseDto like(Long id, Users users){
        Posts posts = checkPosts(id); // 게시글 조회

        Users existUser = checkUser(users); // 사용자 조회

        checkAuthority(existUser,users); //권한 확인 //(ROLE이 ADMIN이든 User든 userId(토큰만) 있으면 좋아요를 할 수 있으니 여기서는 제외)
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

    // 게시글 수정
    public MessageResponseDto updatePost(Long postId, UpdateRequestDto updateRequestDto,Users users) {
        Posts posts = checkPosts(postId); // 게시글 조회
        Users existUser = checkUser(users); // 사용자 조회
        checkAuthority(existUser,posts.getUsers()); //권한 확인 (ROLE 확인 및 게시글 사용자 id와 토큰에서 가져온 사용자 id 일치 여부 확인)
        posts.update(updateRequestDto); // 수정

        return new MessageResponseDto("수정 되었습니다.",HttpServletResponse.SC_OK);
    }

    // 해당 게시물 삭제
    public MessageResponseDto deletePost(Long postId, Users users){
        Posts posts = checkPosts(postId); // 게시글 조회
        Users existUser = checkUser(users); // 사용자 조회
        checkAuthority(existUser,posts.getUsers()); //권한 확인(ROLE 확인 및 게시글 사용자 id와 토큰에서 가져온 사용자 id 일치 여부 확인)

        // 연관된 댓글 삭제(orphanRemoval기능:자동으로 대댓글 삭제)
        List<Comments> commentsList = commentsRepository.findByPosts(posts);
        commentsRepository.deleteAll(commentsList);

        // 연관된 좋아요 테이블 삭제
        List<PostsLike> postsLikeList = postsLikeRepository.findByPosts(posts);
        postsLikeRepository.deleteAll(postsLikeList);

        // 연관된 여행일자들 삭제(CascadeType.REMOVE기능:자동으로 여행 세부일정들 삭제)
        List<Days> daysList = daysRepository.findByPosts(posts);
        daysRepository.deleteAll(daysList);

        postsRepository.delete(posts); // 게시글 삭제
        return new MessageResponseDto("삭제 되었습니다.",HttpServletResponse.SC_OK);
    }

    // 사용자 조회 메서드
    private Users checkUser (Users users) {
        return usersRepository.findByEmail(users.getEmail()).
                orElseThrow(() -> new CustomException(ErrorCode.ID_NOT_MATCH));

    }

    // ADMIN 권한 및 이메일 일치여부 메서드
    private void checkAuthority(Users existUser,Users users){
        if (!existUser.getUserRole().equals(UserRoleEnum.ADMIN)&&!existUser.getEmail().equals(users.getEmail())) {throw new CustomException(ErrorCode.NOT_ALLOWED);
        }

    }

    // 게시글 조회 메서드
    private Posts checkPosts(Long id) {
        return postsRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_EXIST));
    }

    // 해당 게시물에 대한 댓글 조회 메서드
    private List<Comments> checkComments(Posts posts) {
        List<Comments> commentsList = commentsRepository.findByPostsOrderByCreatedAtDesc(posts);
        if(commentsList.isEmpty()){
            throw new CustomException(ErrorCode.COMMENTS_NOT_FOUND);
        }
        return commentsList;
    }
}
