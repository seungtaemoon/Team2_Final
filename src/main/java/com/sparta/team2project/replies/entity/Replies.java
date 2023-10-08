package com.sparta.team2project.replies.entity;

import com.sparta.team2project.comments.entity.Comments;
import com.sparta.team2project.commons.entity.Timestamped;
import com.sparta.team2project.replies.dto.RepliesRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor

@Table(name = "replies")
public class Replies extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="replies_id")
    private  Long id;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, length = 500)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "comments_id")
    private Comments comments;

    public Replies(RepliesRequestDto requestDto) {
        this.nickname = requestDto.getNickname();
        this.contents = requestDto.getContents();
    }

    public void update(RepliesRequestDto requestDto) {
        this.nickname = requestDto.getNickname();
        this.contents = requestDto.getContents();
    }

    public void setComments(Comments comments) {
        this.comments = comments;
    }
}