package com.sparta.team2project.replies.entity;

import com.sparta.team2project.comments.entity.Comments;

import com.sparta.team2project.commons.timestamped.TimeStamped;
import com.sparta.team2project.posts.entity.Posts;
import com.sparta.team2project.replies.dto.RepliesRequestDto;
import com.sparta.team2project.users.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "replies")
public class Replies extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="replies_id")
    private Long id;

    private String nickname;

    @Column(nullable = false, length = 500)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "comments_id")
    private Comments comments;

    public Replies(RepliesRequestDto requestDto, Users users, Comments comments) {
        this.nickname = users.getNickName();
        this.contents = requestDto.getContents();
        this.comments = comments;
    }

    public void update(RepliesRequestDto requestDto, Users users) {
        this.nickname = users.getNickName();
        this.contents = requestDto.getContents();
    }
}