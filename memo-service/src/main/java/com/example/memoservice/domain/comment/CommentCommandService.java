package com.example.memoservice.domain.comment;

import com.example.commonmodule.common.exception.DataNotFoundException;
import com.example.memoservice.domain.comment.model.Comment;
import com.example.memoservice.domain.comment.model.CommentRepository;
import com.example.memoservice.domain.memo.MemoQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentCommandService {

    private final CommentRepository commentRepository;

    private final MemoQueryService memoQueryService;


    public Long createComment(Long postId, CommentCreateRequest request) {
        memoQueryService.existsMemo(postId);

        Comment newComment = new Comment(postId, request.content());
        return commentRepository.save(newComment).getCommentId();
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public void updateComment(Long commentId, CommentUpdateRequest request) {
        Comment comment = getComment(commentId);
        comment.update(request.content());
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundException("Comment Not Found"));
    }

}
