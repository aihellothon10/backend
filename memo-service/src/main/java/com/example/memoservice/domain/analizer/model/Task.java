package com.example.memoservice.domain.analizer.model;

import com.example.commonmodule.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Task extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskType taskType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Column(nullable = false)
    private int sortOrder;

    @Column(name = "input", nullable = true)
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> input;

    @Column(name = "result", nullable = true)
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> result;

    @Column(name = "request")
    @JdbcTypeCode(SqlTypes.JSON)
    @Getter
    @Setter
    private Map<String, Object> request = new HashMap<>();

    @Column(name = "response")
    @JdbcTypeCode(SqlTypes.JSON)
    @Getter
    @Setter
    private Map<String, Object> response = new HashMap<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    @Setter(AccessLevel.PROTECTED)
    private Job job;

    public Task(TaskType taskType, int sortOrder) {
        this.taskType = taskType;
        this.status = TaskStatus.Created;
        this.sortOrder = sortOrder;
    }

//    public Task(TaskType taskType, Map<String, Object> input) {
//        this.taskType = taskType;
//        this.input = input;
//        this.status = TaskStatus.Created;
//    }

    public void setInput(Map<String, Object> input) {
        if (this.input != null) {
            return;
        }
        this.input = input;
    }

    public void setResult(Map<String, Object> result) {
        if (this.result != null) {
            return;
        }
        this.result = result;
    }

    public void updateStatus(TaskStatus status) {
        this.status = status;
    }

}
