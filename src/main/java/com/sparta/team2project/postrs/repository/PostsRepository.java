package com.sparta.team2project.postrs.repository;

import com.sparta.team2project.postrs.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Posts, Long> {
}
