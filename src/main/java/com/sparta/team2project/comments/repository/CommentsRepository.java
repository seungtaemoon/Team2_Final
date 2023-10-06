package com.sparta.team2project.comments.repository;

import org.hibernate.annotations.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
}
