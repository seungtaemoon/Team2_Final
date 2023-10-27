package com.sparta.team2project.comments;

import com.sparta.team2project.comments.dto.CommentsRequestDto;
import com.sparta.team2project.comments.entity.Comments;
import com.sparta.team2project.comments.repository.CommentsRepository;
import com.sparta.team2project.comments.service.CommentsService;
import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.entity.UserRoleEnum;
import com.sparta.team2project.commons.exceptionhandler.CustomException;
import com.sparta.team2project.commons.exceptionhandler.ErrorCode;
import com.sparta.team2project.posts.entity.PostCategory;
import com.sparta.team2project.posts.entity.Posts;
import com.sparta.team2project.posts.repository.PostsRepository;
import com.sparta.team2project.users.UserRepository;
import com.sparta.team2project.users.Users;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommentsServiceTest {

    // @Mock 이 붙은 목 객체를 주입시킬 수 있다
    @InjectMocks
    private CommentsService commentsService;

    // 로직이 삭제된 빈껍데기, 실제로 메서드는 가지고 있지만 내부구현이 없음
    @Mock
    private CommentsRepository commentsRepository;

    @Mock
    private PostsRepository postsRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    public CommentsRequestDto McokCommentsRequestDto() {
        CommentsRequestDto commentsRequestDto = mock(CommentsRequestDto.class);
        when(commentsRequestDto.getContents()).thenReturn("댓글");
        return commentsRequestDto;
    }

    public Users MockUsers1(){
        return new Users("rkawk@email.com", "감자", "test123!", UserRoleEnum.USER, "image/profileImg.png");
    }
    public Users MockUsers2(){
        return new Users("qksksk@email.com", "바나나", "test123!", UserRoleEnum.USER, "image/profileImg.png");
    }
    public Users MockAdmin(){
        return new Users("rhksflwk@email.com", "관리자", "test123!", UserRoleEnum.ADMIN, "image/profileImg.png");
    }

    public Posts MockPosts(){
        return new Posts("해돋이 보러간다", "정동진 해돋이", PostCategory.가족, "동해안 해돋이", MockUsers1());
    }

    public Comments MockComments() {
        return new Comments(McokCommentsRequestDto(), MockUsers1(), MockPosts());
    }


    @Test
    @DisplayName("[정상 작동] 댓글 생성")
    public void commentsCreateTest() {

        // Given
        Long postId = 1L;
        CommentsRequestDto requestDto = McokCommentsRequestDto();
        Users users = MockUsers1(); // 사용자 정보 초기화
        Posts post = new Posts(); // 게시글 정보 초기화

        when(postsRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentsRepository.save(any(Comments.class))).thenReturn(new Comments());

        // When
        MessageResponseDto response = commentsService.commentsCreate(postId, requestDto, users);

        // Then
        System.out.println("댓글 생성");
        assertEquals("댓글을 작성하였습니다", response.getMsg());
        assertEquals(HttpServletResponse.SC_OK, response.getStatusCode());
    }

    @Test
    @DisplayName("[비정상 작동]댓글 생성")
    public void commentsCreatePostNotFoundTest() {

        // Given
        Long postId = 1L;
        CommentsRequestDto requestDto = McokCommentsRequestDto();
        Users users = MockUsers1(); // 사용자 정보 초기화

        when(postsRepository.findById(postId)).thenReturn(Optional.empty());

        // When
        CustomException exception = assertThrows(CustomException.class, () -> {
            commentsService.commentsCreate(postId, requestDto, users);
        });

        // Then
        System.out.println("ErrorCode.POST_NOT_EXIST: " + "존재하지 않는 게시글입니다");
        System.out.println("exception.getErrorCode(): " + exception.getErrorCode());
        assertEquals(ErrorCode.POST_NOT_EXIST, exception.getErrorCode());
    }

    @Test
    @DisplayName("[정상 작동] 본인이 쓴 댓글 수정 USER")
    public void commentsUpdateUserTest() {

        // Given
        Long commentId = 1L;
        CommentsRequestDto requestDto = McokCommentsRequestDto();
        Users users = MockUsers1(); // 사용자 정보 초기화
        Posts posts = MockPosts(); // 게시글 정보 초기화
        Comments comments = MockComments(); // 댓글 정보 초기화

        when(commentsRepository.findById(commentId)).thenReturn(Optional.of(comments));
        when(userRepository.findByEmail(users.getEmail())).thenReturn(Optional.of(posts.getUsers()));

        // When
        MessageResponseDto response = commentsService.commentsUpdate(commentId, requestDto, users);

        // Then
        System.out.println("댓글 수정");
        assertNotNull(response);
        assertEquals("댓글을 수정하였습니다", response.getMsg());
        assertEquals(HttpServletResponse.SC_OK, response.getStatusCode());
    }

    @Test
    @DisplayName("[정상 작동] 모든 댓글 수정 ADMIN")
    public void commentsUpdateAdminTest() {

        // Given
        Long commentId = 1L;
        CommentsRequestDto requestDto = McokCommentsRequestDto();
        Users users = MockUsers1(); // 사용자 정보 초기화
        Users users1 = MockAdmin(); // 사용자 정보 초기화
        Posts posts = MockPosts(); // 게시글 정보 초기화
        Comments comments = MockComments(); // 댓글 정보 초기화

        when(commentsRepository.findById(commentId)).thenReturn(Optional.of(comments));
        when(userRepository.findByEmail(users.getEmail())).thenReturn(Optional.of(posts.getUsers()));

        // When
        MessageResponseDto response = commentsService.commentsUpdate(commentId, requestDto, users1);

        // Then
        System.out.println("관리자 댓글 수정");
        assertNotNull(response);
        assertEquals("관리자가 댓글을 수정하였습니다", response.getMsg());
        assertEquals(HttpServletResponse.SC_OK, response.getStatusCode());
    }

    @Test
    @DisplayName("[비정상 작동]댓글 수정 본인이 쓴 댓글 아닌 경우")
    public void commentsUpdateNotAllowedTest() {

        // Given
        Long commentId = 1L;
        CommentsRequestDto requestDto = McokCommentsRequestDto();
        Users users2 = MockUsers2(); // 사용자 정보 초기화
        Comments comments = MockComments(); // 댓글 정보 초기화

        when(comments.getEmail()).thenReturn("다른 사용자의 이메일");
        when(commentsRepository.findById(commentId)).thenReturn(Optional.of(comments));

        // When
        CustomException exception = assertThrows(CustomException.class, () -> {
            commentsService.commentsUpdate(commentId, requestDto, users2);
        });

        // Then
        System.out.println("ErrorCode.NOT_ALLOWED: " + "권한이 없습니다");
        System.out.println("exception.getErrorCode(): " + exception.getErrorCode());
        assertEquals(ErrorCode.NOT_ALLOWED, exception.getErrorCode());
    }

    @Test
    @DisplayName("[정상 작동] 본인이 쓴 댓글 삭제 USER")
    public void commentsDeleteUserTest() {

        // Given
        Long commentId = 1L;
        Users users = MockUsers1(); // 사용자 정보 초기화
        Posts posts = MockPosts(); // 게시글 정보 초기화
        Comments comments = MockComments(); // 댓글 정보 초기화

        when(commentsRepository.findById(commentId)).thenReturn(Optional.of(comments));
        when(userRepository.findByEmail(users.getEmail())).thenReturn(Optional.of(posts.getUsers()));

        // When
        MessageResponseDto response = commentsService.commentsDelete(commentId, users);

        // Then
        System.out.println("댓글 삭제");
        assertNotNull(response);
        assertEquals("댓글을 삭제하였습니다", response.getMsg());
        assertEquals(HttpServletResponse.SC_OK, response.getStatusCode());
    }

    @Test
    @DisplayName("[정상 작동] 모든 댓글 삭제 ADMIN")
    public void commentsDeleteAdminTest() {

        // Given
        Long commentId = 1L;
        Users users = MockUsers1(); // 사용자 정보 초기화
        Users users1 = MockAdmin(); // 사용자 정보 초기화
        Posts posts = MockPosts(); // 게시글 정보 초기화
        Comments comments = MockComments(); // 댓글 정보 초기화

        when(commentsRepository.findById(commentId)).thenReturn(Optional.of(comments));
        when(userRepository.findByEmail(users.getEmail())).thenReturn(Optional.of(posts.getUsers()));

        // When
        MessageResponseDto response = commentsService.commentsDelete(commentId, users1);

        // Then
        System.out.println("관리자 댓글 삭제");
        assertNotNull(response);
        assertEquals("관리자가 댓글을 삭제하였습니다", response.getMsg());
        assertEquals(HttpServletResponse.SC_OK, response.getStatusCode());
    }

    @Test
    @DisplayName("[비정상 작동]댓글 삭제 본인이 쓴 댓글 아닌 경우")
    public void commentsDeleteNotAllowedTest() {

        // Given
        Long commentId = 1L;
        Users users2 = MockUsers2(); // 사용자 정보 초기화
        Comments comments = MockComments(); // 댓글 정보 초기화

        when(comments.getEmail()).thenReturn("다른 사용자의 이메일");
        when(commentsRepository.findById(commentId)).thenReturn(Optional.of(comments));

        // When
        CustomException exception = assertThrows(CustomException.class, () -> {
            commentsService.commentsDelete(commentId, users2);
        });

        // Then
        System.out.println("ErrorCode.NOT_ALLOWED: " + "권한이 없습니다");
        System.out.println("exception.getErrorCode(): " + exception.getErrorCode());
        assertEquals(ErrorCode.NOT_ALLOWED, exception.getErrorCode());
    }
}
