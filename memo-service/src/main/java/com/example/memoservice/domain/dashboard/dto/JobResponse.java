package com.example.memoservice.domain.dashboard.dto;

import com.example.memoservice.domain.analizer.model.Job;
import com.example.memoservice.domain.analizer.model.Task;
import com.example.memoservice.domain.analizer.model.TaskStatus;
import com.example.memoservice.domain.analizer.model.TaskType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class JobResponse {

    private Long jobId;

    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

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

    public static JobResponse of(Job job) {
        return new JobResponse(
                job.getJobId(),
                job.getCreatedAt(),
                job.getUpdatedAt(),
                job.getTasks().stream().map(TaskDto::of).toList()
        );
    }

}
