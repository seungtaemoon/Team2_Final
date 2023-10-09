package com.sparta.team2project.comments.entity;

import com.sparta.team2project.comments.dto.CommentsRequestDto;

import com.sparta.team2project.commons.timestamped.TimeStamped;
import com.sparta.team2project.posts.entity.Posts;
import com.sparta.team2project.replies.entity.Replies;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "comments")
public class Comments extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="comments_id")
    private  Long id;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, length = 500)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "posts_id")
    private Posts posts;

    @OneToMany (mappedBy = "comments", orphanRemoval = true)
    @OrderBy("createdAt asc")
    private List<Replies> repliesList= new ArrayList<>();


    public Comments(CommentsRequestDto requestDto) {
        this.nickname = requestDto.getNickname();
        this.contents = requestDto.getContents();
    }

    public void update(CommentsRequestDto requestDto) {
        this.nickname = requestDto.getNickname();
        this.contents = requestDto.getContents();
    }

    public void addReplies(Replies newReplies) {
        this.repliesList.add(newReplies);
        newReplies.setComments(this);
    }

    public void setPosts(Posts posts) {
        this.posts = posts;
    }
}
