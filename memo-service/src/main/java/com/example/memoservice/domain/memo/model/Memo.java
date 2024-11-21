package com.example.memoservice.domain.memo.model;

import com.example.commonmodule.common.entity.BaseEntity;
import com.mysema.commons.lang.Assert;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Memo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memoId;

    /**
     * Document를 생성하려면
     */
    @Column(name = "document_id", nullable = false, unique = true, updatable = false, length = 40)
    private String documentId;

    /**
     * 분석 ID
     */
    @Column(name = "job_id", nullable = false)
    private Long jobId;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "evaluation_status", nullable = false, length = 20)
    private EvaluationStatus evaluationStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_audience", nullable = false, length = 20)
    private TargetAudience targetAudience;

    /**
     * 사용자가 입력한 내용(링크, 전문)
     */
    @Column
    private String userInput;

    /**
     * 입력한 내용을 기반으로 분석한 결과
     */
    @Column(name = "analysis_summary", length = 1000)
    private String analysisSummary;

    @Column(name = "source", nullable = false, length = 200)
    private String source;

    @Column(name = "hidden", nullable = false)
    private boolean hidden = false;

    @Builder
    public Memo(String documentId,
                String title,
                String content,
                EvaluationStatus evaluationStatus,
                TargetAudience targetAudience,
                String userInput,
                String analysisSummary,
                String source) {
        Assert.hasText(documentId, "documentId must not be empty");
        Assert.hasText(title, "title must not be empty");
        Assert.hasText(content, "content must not be empty");
        Assert.notNull(evaluationStatus, "evaluationStatus must not be null");
        Assert.notNull(targetAudience, "targetAudience must not be null");
        Assert.hasText(userInput, "userInput must not be null");
        Assert.hasText(analysisSummary, "analysisSummary must not be null");
        Assert.hasText(source, "source must not be null");

        this.documentId = documentId;
        this.title = title;
        this.content = content;
        this.evaluationStatus = evaluationStatus;
        this.targetAudience = targetAudience;
        this.userInput = userInput;
        this.analysisSummary = analysisSummary;
        this.source = source;
        this.hidden = false;
    }


}
