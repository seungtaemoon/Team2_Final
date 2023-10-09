package com.sparta.team2project.days.repository;

import com.sparta.team2project.days.entity.Days;
import com.sparta.team2project.posts.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DaysRepository extends JpaRepository<Days, Long> {

    List<Days> findByPosts(Posts posts);
}
