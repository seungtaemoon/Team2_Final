package com.sparta.team2project.postslike.entity;

import com.sparta.team2project.posts.entity.Posts;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "posts_like")
public class PostsLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="posts_id")
    private Posts posts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    public BoardLike(Posts posts, Users users) {
        this.posts = posts;
        this.users = users;

    }
}
