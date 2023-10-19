package com.sparta.team2project.posts.repository;

import com.sparta.team2project.posts.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface PostsRepository extends JpaRepository<Posts, Long> {
    Page<Posts> findAllByOrderByModifiedAtDesc(Pageable pageable);

    List<Posts> findFirst3ByOrderByLikeNumDescModifiedAtDesc();

    Set<Posts> findByTitleContaining(String keyword);
}