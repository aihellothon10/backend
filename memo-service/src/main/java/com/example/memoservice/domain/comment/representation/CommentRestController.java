package com.example.memoservice.domain.comment.representation;

import com.example.memoservice.domain.comment.CommentCommandService;
import com.example.memoservice.domain.comment.CommentCreateRequest;
import com.example.memoservice.domain.comment.CommentQueryService;
import com.example.memoservice.domain.comment.CommentUpdateRequest;
import com.example.memoservice.domain.comment.model.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentRestController {

    private final CommentCommandService commentCommandService;
    private final CommentQueryService commentQueryService;

    @PostMapping("/api/memos/{memoId}/comments")
    CommentIdResponse createComment(@PathVariable Long memoId,
                                    @RequestBody CommentCreateRequest request) {
        return new CommentIdResponse(commentCommandService.createComment(memoId, request));
    }

    @GetMapping("/api/memos/{memoId}/comments")
    PagedModel<Comment> getComments(@PathVariable Long memoId,
                                    @RequestParam(value = "page", defaultValue = "0") int page,
                                    @RequestParam(value = "size", defaultValue = "10") int size) {
        return new PagedModel<>(commentQueryService.getCommentsByMemoId(memoId, page, size));
    }

    @DeleteMapping("/api/comments/{commentId}")
    void deleteComment(@PathVariable Long commentId) {
        commentCommandService.deleteComment(commentId);
    }

    @PutMapping("/api/comments/{commentId}")
    void updateComment(@PathVariable Long commentId, @RequestBody CommentUpdateRequest request) {
        commentCommandService.updateComment(commentId, request);
    }

    record CommentIdResponse(Long commentId) {
    }

}
