package com.example.memoservice.domain.dashboard;

import com.example.memoservice.domain.analizer.JobQueryService;
import com.example.memoservice.domain.analizer.TaskQueryService;
import com.example.memoservice.domain.dashboard.dto.JobResponse;
import com.example.memoservice.domain.dashboard.dto.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
class DashboardRestController {

    private final JobQueryService jobQueryService;
    private final TaskQueryService taskQueryService;

    @GetMapping("/jobs/{jobId}")
    JobResponse getJobs(@PathVariable Long jobId) {
        return JobResponse.of(jobQueryService.getJob(jobId));
    }

    @GetMapping("/tasks/{taskId}")
    TaskResponse getTaskDetail(@PathVariable Long taskId) {
        return TaskResponse.of(taskQueryService.getTaskByTaskId(taskId));
    }


}
