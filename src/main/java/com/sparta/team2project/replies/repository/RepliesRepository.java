package com.sparta.team2project.replies.repository;

import com.sparta.team2project.comments.entity.Comments;
import com.sparta.team2project.replies.entity.Replies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepliesRepository extends JpaRepository<Replies, Long> {
    List<Replies> findAllByCommentsOrderByCreatedAtDesc(Comments comments);

    Slice<Replies> findByComments_IdOrderByCreatedAtDesc(Long commentId, Pageable pageable);

    Slice<Replies> findByComments_IdAndEmailOrderByCreatedAtDesc(Long commentId, String email, Pageable pageable);
}
