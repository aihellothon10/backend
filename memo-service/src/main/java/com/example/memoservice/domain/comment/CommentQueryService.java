package com.example.memoservice.domain.comment;

import com.example.commonmodule.common.exception.DataNotFoundException;
import com.example.memoservice.domain.comment.model.Comment;
import com.example.memoservice.domain.comment.model.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentQueryService {

    private final CommentRepository commentRepository;

    public Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundException("Comment Not Found"));
    }

    public Page<Comment> getCommentsByMemoId(Long memoId, int page, int size) {
        return commentRepository.findByMemoId(memoId, PageRequest.of(page, size));
    }

}
