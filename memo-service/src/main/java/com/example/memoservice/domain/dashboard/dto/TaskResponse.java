package com.example.memoservice.domain.dashboard.dto;

import com.example.memoservice.domain.analizer.model.Task;
import com.example.memoservice.domain.analizer.model.TaskStatus;
import com.example.memoservice.domain.analizer.model.TaskType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class TaskResponse {
    private Long taskId;
    private TaskStatus status;
    private TaskType taskType;
    private int sortOrder;
    private Map<String, Object> input;
    private Map<String, Object> result;
    private Map<String, Object> request;
    private Map<String, Object> response;

    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;


    public static TaskResponse of(Task task) {
        return new TaskResponse(
                task.getTaskId(),
                task.getStatus(),
                task.getTaskType(),
                task.getSortOrder(),
                task.getInput(),
                task.getResult(),
                task.getRequest() == null ? new HashMap<>() : task.getRequest(),
                task.getResponse() == null ? new HashMap<>() : task.getResponse(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }

}
