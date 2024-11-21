package com.example.memoservice.domain.memo;

import com.example.memoservice.domain.comment.model.Comment;
import com.example.memoservice.domain.memo.model.EvaluationStatus;
import com.example.memoservice.domain.memo.model.Memo;
import com.example.memoservice.domain.memo.model.TargetAudience;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class MemoDetail {

    private String documentId;
    private String title;
    private String content;
    private EvaluationStatus evaluationStatus;
    private TargetAudience targetAudience;
    private String userInput;
    private String analysisSummary;
    private String source;
    private List<CommentDetail> comments;


    public static MemoDetail of(Memo memo, Page<Comment> comments) {
        MemoDetail memoDetail = new MemoDetail();
        memoDetail.documentId = memo.getDocumentId();
        memoDetail.title = memo.getTitle();
        memoDetail.content = memo.getContent();
        memoDetail.evaluationStatus = memo.getEvaluationStatus();
        memoDetail.targetAudience = memo.getTargetAudience();
        memoDetail.userInput = memo.getUserInput();
        memoDetail.analysisSummary = memo.getAnalysisSummary();
        memoDetail.source = memo.getSource();
        memoDetail.comments = comments.stream()
                .map(comment -> new CommentDetail(comment.getContent()))
                .toList();
        return memoDetail;
    }

    record CommentDetail(String content) {
    }

}
