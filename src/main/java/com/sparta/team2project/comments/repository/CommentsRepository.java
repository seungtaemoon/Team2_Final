package com.sparta.team2project.comments.repository;


import com.sparta.team2project.comments.entity.Comments;
import com.sparta.team2project.posts.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comments, Long> {

    List<Comments> findByIdOrderByCreatedAtDesc(Long postId);

    List<Comments> findByPostsOrderByCreatedAtDesc(Posts posts);

    List<Comments> findByPosts(Posts posts);
}

