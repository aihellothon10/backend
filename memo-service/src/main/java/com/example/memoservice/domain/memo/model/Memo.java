package com.example.memoservice.domain.memo.model;

import com.example.commonmodule.common.entity.BaseEntity;
import com.example.memoservice.domain.analizer.model.AnalyzeInput;
import com.example.memoservice.domain.analizer.model.AnalyzeResult;
import com.mysema.commons.lang.Assert;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Memo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memoId;

    private Long jobId;

    /**
     * Document를 생성한다면 id를 지정한다.
     * // TODO 생성하지 않아도 되게 unique를 false로
     */
    @Column(name = "document_id", nullable = false, unique = false, updatable = false, length = 40)
    private String documentId;

    @Embedded
    private AnalyzeInput analyzeInput;

    @Embedded
    private AnalyzeResult analyzeResult;

    // 메모에 해당하는 아기
    @ElementCollection
    @CollectionTable(name = "target_babies", joinColumns = @JoinColumn(name = "memo_id"))
    private List<String> targetBabies;

    @ElementCollection
    @CollectionTable(name = "target_members", joinColumns = @JoinColumn(name = "memo_id"))
    private List<String> targetMembers;

    // 메모를 볼 대상
//    @Enumerated(EnumType.STRING)
//    @ElementCollection
    @ElementCollection
    @CollectionTable(name = "target_audiences", joinColumns = @JoinColumn(name = "memo_id"))
    private List<String> targetAudiences;

    @Enumerated(EnumType.STRING)
    private MemoStatus memoStatus;


    @Builder
    public Memo(Long jobId,
                String documentId,
                AnalyzeInput analyzeInput,
                AnalyzeResult analyzeResult,
                List<String> targetBabies,
                List<String> targetMembers,
                List<String> targetAudiences) {
        Assert.hasText(documentId, "documentId must not be empty");
        this.jobId = jobId;
        this.documentId = documentId;
        this.analyzeInput = analyzeInput;
        this.analyzeResult = analyzeResult;
        this.targetBabies = targetBabies;
        this.targetMembers = targetMembers;
        this.targetAudiences = targetAudiences;
        this.memoStatus = MemoStatus.Normal;
    }

    public void favorite() {
        this.memoStatus = MemoStatus.Favorite;
    }

    public void hidden() {
        this.memoStatus = MemoStatus.Hidden;
    }

}
