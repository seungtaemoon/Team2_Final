package com.sparta.team2project.posts.repository;

import com.sparta.team2project.posts.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {
    List<Posts> findAllByOrderByCreatedAtDesc();
}
