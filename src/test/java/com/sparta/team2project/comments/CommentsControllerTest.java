package com.sparta.team2project.comments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.team2project.comments.controller.CommentsController;
import com.sparta.team2project.comments.dto.CommentsRequestDto;
import com.sparta.team2project.commons.MockSpringSecurityFilter;
import com.sparta.team2project.commons.config.WebSecurityConfig;
import com.sparta.team2project.commons.dto.MessageResponseDto;
import com.sparta.team2project.commons.security.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest (
        controllers = {CommentsController.class},
        // 제외 할 것 지정
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)

class CommentsControllerTest {
    private MockMvc mvc;

    private Principal principal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    CommentsController commentsController;

    @BeforeEach
    public void setup() {

        mvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new MockSpringSecurityFilter())
                .apply(springSecurity()) // 우리가 만든 필터 넣기
                .build();
    }

    // 테스트할 USER 객체
    private void mockUserSetup() {
        // Mock 테스트 유저 생성
        UserDetails userDetails = User.withUsername("rkawk@gmail.com")
                .password("kim881012!!!")
                .roles("USER") // 롤에 대한 설정
                .build();
        principal = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }

//    @WithMockUser
    @Test
    @DisplayName("댓글 생성")
    public void commentsCreateTest () throws Exception {
        mockUserSetup();
        // Given
        Long postId =1L;
        String contents ="댓글";
        CommentsRequestDto requestDto = new CommentsRequestDto(contents);

        // eq : 특정한 값을 기대하는 경우에 사용됨
        // any : 어떤 값이든 허용하는 경우에 사용됨
        when(commentsController.commentsCreate(eq(postId), any(CommentsRequestDto.class), any(UserDetailsImpl.class)))
                .thenReturn(new ResponseEntity<>(new MessageResponseDto("댓글이 생성되었습니다.", 200), HttpStatus.OK));

        // Wen and Then
        mvc.perform(MockMvcRequestBuilders.post("/api/posts/"+postId+"/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()) // CSRF 토큰을 요청에 포함
                        .principal(principal)) // 가짜 사용자 principal 설정
                        .andExpect(status().isOk());
    }
}
