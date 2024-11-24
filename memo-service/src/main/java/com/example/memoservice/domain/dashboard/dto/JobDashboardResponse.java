package com.example.memoservice.domain.dashboard.dto;

import com.example.memoservice.domain.analizer.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class JobDashboardResponse {

    private Long jobId;

    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private AnalysisStatus status;

    private List<TaskDto> tasks;

    @Getter
    @AllArgsConstructor
    public static class TaskDto {
        private Long taskId;
        private TaskStatus status;
        private TaskType taskType;

        public static TaskDto of(Task task) {
            return new TaskDto(
                    task.getTaskId(),
                    task.getStatus(),
                    task.getTaskType()
            );
        }
    }

    public static JobDashboardResponse of(Job job) {
        return new JobDashboardResponse(
                job.getJobId(),
                job.getCreatedAt(),
                job.getUpdatedAt(),
                job.getStatus(),
                job.getTasks().stream().map(TaskDto::of).toList()
        );
    }

}
