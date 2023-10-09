package com.sparta.team2project.replies.repository;

import com.sparta.team2project.comments.entity.Comments;
import com.sparta.team2project.replies.entity.Replies;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface RepliesRepository extends JpaRepository<Replies, Long> {

    List<Replies> findByIdComments_IdOrderByCreatedAtDesc(Long commentId);

    List<Replies> findAllByCommentsOrderByCreatedAtDesc(Comments comments);
}
