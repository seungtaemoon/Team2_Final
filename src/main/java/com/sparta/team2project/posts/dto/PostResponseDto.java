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
    private final List<String> tagsList;
    private final String contents;
    private final String nickName;
    private final int likeNum;
    private final int viewNum;
    private final PostCategory postCategory;
    private final int commentNum;
    private final LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    //private List<PostDetailResponseDto> commentList;

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

    public PostResponseDto(Posts posts, Users users,List<String> tagsList,int commentNum){
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
        this.modifiedAt = posts.getModifiedAt();
        //this.commentList = dtoList;
    }


}
