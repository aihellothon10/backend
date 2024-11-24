package com.example.memoservice.domain.memo;

import com.example.memoservice.domain.analizer.model.AnalyzeInput;
import com.example.memoservice.domain.analizer.model.AnalyzeResult;
import com.example.memoservice.domain.comment.model.Comment;
import com.example.memoservice.domain.memo.model.Memo;
import com.example.memoservice.domain.memo.model.MemoStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class MemoDetail {

    private String documentId;
    private List<String> babyIds;
    private List<String> targetAudiences;

    private MemoStatus memoStatus;

    private List<CommentDetail> comments;

    private AnalyzeInput analyzeInput;
    private AnalyzeResult analyzeResult;


    public static MemoDetail of(Memo memo, Page<Comment> comments) {
        MemoDetail memoDetail = new MemoDetail();
        memoDetail.documentId = memo.getDocumentId();
        memoDetail.babyIds = memo.getTargetBabies();
        memoDetail.targetAudiences = memo.getTargetAudiences();
        memoDetail.memoStatus = memo.getMemoStatus();

        memoDetail.comments = comments.stream()
                .map(comment -> new CommentDetail(comment.getContent()))
                .toList();
        memoDetail.analyzeInput = memo.getAnalyzeInput();
        memoDetail.analyzeResult = memo.getAnalyzeResult();

        return memoDetail;
    }

    record CommentDetail(String content) {
    }

}
