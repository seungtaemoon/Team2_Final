package com.sparta.team2project.postslike.repository;

import com.sparta.team2project.posts.entity.Posts;
import com.sparta.team2project.postslike.entity.PostsLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsLikeRepository extends JpaRepository<PostsLike,Long> {
    PostsLike findByPostsAndUsers(Posts posts, Users users);
}
