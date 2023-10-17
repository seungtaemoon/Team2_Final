package com.sparta.team2project.posts.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.team2project.posts.entity.PostCategory;
import com.sparta.team2project.posts.entity.Posts;
import com.sparta.team2project.users.Users;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponseDto {

    private final Long postId;
    private final String title;
    private final String contents;
    private final String nickName;
    private final LocalDateTime createdAt;
    private List<String> tagsList;
    private Integer likeNum;
    private Integer viewNum;
    private PostCategory postCategory;
    private Integer commentNum;
    private LocalDateTime modifiedAt;

     // 전체 게시글 관련 반환시
    public PostResponseDto(Posts posts, List<String> tagsList,Users users,int commentNum){
        this.postId = posts.getId();
        this.title = posts.getTitle();
        this.tagsList = tagsList;
        this.contents = posts.getContents();
        this.nickName = users.getNickName();
        this.likeNum = posts.getLikeNum();
        this.viewNum =  posts.getViewNum();
        this.commentNum = commentNum;
        this.postCategory = posts.getPostCategory();
        this.createdAt = posts.getCreatedAt();
    }
    // 상세 게시글 관련 반환시
    public PostResponseDto(Posts posts, Users users, List<String> tagsList, int commentNum, LocalDateTime modifiedAt){
        this.postId = posts.getId();
        this.title = posts.getTitle();
        this.tagsList = tagsList;
        this.contents = posts.getContents();
        this.nickName = users.getNickName();
        this.likeNum = posts.getLikeNum();
        this.viewNum =  posts.getViewNum();
        this.commentNum = commentNum;
        this.postCategory = posts.getPostCategory();
        this.createdAt = posts.getCreatedAt();
        this.modifiedAt = modifiedAt;
    }

    // 사용자가 누른 게시물 관련 반환시
    public PostResponseDto(Posts posts,Users users){
        this.postId = posts.getId();
        this.title = posts.getTitle();
        this.contents = posts.getContents();
        this.nickName = users.getNickName();
        this.createdAt = posts.getCreatedAt();
    }
}
