package com.sparta.team2project.tags.repository;

import com.sparta.team2project.posts.entity.Posts;
import com.sparta.team2project.tags.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface TagsRepository extends JpaRepository<Tags, Long> {
     List<Tags> findByPosts(Posts posts);

     Set<Tags> findByPurposeContaining(String keyword);
}
