package com.example.memoservice.domain.analizer;

import com.example.commonmodule.common.exception.DataNotFoundException;
import com.example.memoservice.domain.analizer.model.Task;
import com.example.memoservice.domain.analizer.model.TaskStatus;
import com.example.memoservice.domain.analizer.model.TaskType;
import com.example.memoservice.domain.analizer.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskQueryService {

    private final TaskRepository taskRepository;


    public List<Task> getTasks(Long jobId) {
        return taskRepository.findAllByJobJobId(jobId);
    }

    public Optional<Task> getTaskByTaskType(Long jobId, TaskType taskType) {
        return getTasks(jobId).stream()
                .filter(t -> t.getTaskType() == taskType)
                .findFirst();
    }

    public Task getTaskByTaskId(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new DataNotFoundException("Task Not Found"));
    }

    public Task getNullableTaskByTaskId(Long taskId) {
        return taskRepository.findById(taskId).orElse(null);
    }

    public boolean isCompleted(Long jobId) {
        return getTasks(jobId).stream()
                .allMatch(t -> t.getStatus() == TaskStatus.Completed);
    }

    public Task getNextTask(Long jobId, Long taskId) {
        List<Task> tasks = getTasks(jobId);

        // 현재 task의  sortOrder 기준으로 다음 위치에 있는 task return
        // sortOrder는 0보다 큰 정수이며 작은 수부터 큰수 순서로 next 선택
        Task currentTask = tasks.stream()
                .filter(task -> task.getTaskId().equals(taskId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Task not found for id: " + taskId));
        return tasks.stream()
                .filter(task -> task.getSortOrder() > currentTask.getSortOrder())
                .min(Comparator.comparingInt(Task::getSortOrder))
                .orElse(null);
    }

    public record TaskRequestDto(Long taskId,
                                 TaskType taskType,
                                 TaskStatus taskStatus,
                                 Map<String, Object> input) {
        public static TaskRequestDto of(Task task) {
            return new TaskRequestDto(
                    task.getTaskId(),
                    task.getTaskType(),
                    task.getStatus(),
                    task.getInput()
            );
        }
    }


}
