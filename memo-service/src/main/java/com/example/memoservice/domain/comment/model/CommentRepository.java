package com.example.memoservice.domain.comment.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

//    List<Comment> findByMemoId(Long postId);

//    List<Comment> findByMemberId(Long memberId);

    Page<Comment> findByMemoId(Long postId, Pageable pageable);
}
