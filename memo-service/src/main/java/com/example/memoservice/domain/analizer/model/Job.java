package com.example.memoservice.domain.analizer.model;


import com.example.commonmodule.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Job extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnalysisStatus status;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder asc")
    private List<Task> tasks = new ArrayList<>();

    public Job(String name, JobType jobType) {
        this.name = name;
        this.jobType = jobType;
        this.status = AnalysisStatus.Created;
    }

    // 연관 관계 편의 메서드
    public void addTask(Task task) {
        tasks.add(task);
        task.setJob(this);
    }

    public void updateStatus(AnalysisStatus status) {
        this.status = status;
    }

}
