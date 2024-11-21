package com.example.memoservice.domain.analizer;

import com.example.memoservice.domain.analizer.model.Task;
import com.example.memoservice.domain.analizer.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskCommandService {

    private final TaskRepository taskRepository;
    

    public void setInput(Long taskId, Map<String, Object> data) {
        Task task = findTask(taskId);
        task.setInput(data);
    }

    private Task findTask(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task Not Found"));
    }

    public void setRequestResponse(Task task, Map<String, Object> request, Map<String, Object> response) {
        task.setRequest(request);
        task.setResponse(response);
    }

}
