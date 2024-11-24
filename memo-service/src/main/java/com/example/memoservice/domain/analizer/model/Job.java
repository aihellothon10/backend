package com.example.memoservice.domain.analizer.model;


import com.example.commonmodule.common.entity.BaseEntity;
import com.example.memoservice.domain.apiclient.elice.ToxicityPredictionResponse;
import com.example.memoservice.domain.apiclient.naversearch.dto.NaverSearchResult;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.proxy.HibernateProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Job extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnalysisStatus status;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder asc")
    private List<Task> tasks = new ArrayList<>();

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "chat_room_id")
//    @Setter
//    private ChatRoom chatRoom;

    // ================================= //
    // 사용자 입력
    @Embedded
    private AnalyzeInput analyzeInput;
    // ================================= //
    // 분석 결과
    @Embedded
    private AnalyzeResult analyzeResult = new AnalyzeResult();
    // ================================= //

    public Job(JobType jobType,
               String query,
               List<String> images,
               List<String> links) {
        this.jobType = jobType;
        this.status = AnalysisStatus.Created;

        this.setInputData(query, images, links);
    }

    // 연관 관계 편의 메서드
    public void addTask(Task task) {
        tasks.add(task);
        task.setJob(this);
    }

    public void setInputData(String query,
                             List<String> images,
                             List<String> links) {
        this.analyzeInput = new AnalyzeInput(images, query, links);
    }

//    public void setAnalyzeResult(String generatedTitle,
//                                 String answer,
//                                 Integer contentContainBoolean,
//                                 String contentContainBooleanExplain) {
//        this.analyzeResult = new AnalyzeResult(
//                generatedTitle,
//                answer,
//                contentContainBoolean,
//                contentContainBooleanExplain
//        );
//    }

    public void setDiffBotResult(List<String> crawlingContents,
                                 List<String> crawlingImages) {
        this.analyzeResult.setDiffBotResult(crawlingContents, crawlingImages);
    }

    public void setHelpyVResult(String assistantContent,
                                String generatedTitle,
                                String answer,
                                Integer contentContainBoolean,
                                String contentContainBooleanExplain) {
        this.analyzeResult.setHelpyVResult(
                assistantContent,
                generatedTitle,
                answer,
                contentContainBoolean,
                contentContainBooleanExplain);
    }

    public void setToxicityResult(List<String> toxicityTexts,
                                  List<ToxicityPredictionResponse> toxicityPredictions) {
        this.analyzeResult.setToxicityResult(toxicityTexts, toxicityPredictions);
    }

    public void setPerplexityResult(String content,
                                    List<String> citations) {
        this.analyzeResult.setPerplexityResult(content, citations);
    }

    public void setNaverSearchResult(String keyword,
                                     List<NaverSearchResult.Item> items) {
        this.analyzeResult.setNaverSearchResult(keyword, items);
    }

    public void updateStatus(AnalysisStatus status) {
        this.status = status;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Job job = (Job) o;
        return getJobId() != null && Objects.equals(getJobId(), job.getJobId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
