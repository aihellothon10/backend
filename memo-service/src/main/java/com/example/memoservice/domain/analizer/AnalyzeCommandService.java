package com.example.memoservice.domain.analizer;

import com.example.memoservice.domain.analizer.event.TaskStartEvent;
import com.example.memoservice.domain.analizer.model.JobType;
import com.example.memoservice.domain.analizer.model.TaskType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AnalyzeCommandService {

    private final JobCommandService jobCommandService;
    private final TaskCommandService taskCommandService;
    private final TaskQueryService taskQueryService;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Job을 생성하고 분석을 위해 API 호출
     * - 분석 순서에 맞게 호출해야한다.
     * 1. (Optional) diff bot의로 link 분석
     * 2. helpyv로 분석
     * 3. Perplexity로  2번 결과룰 기반으로 출처링크, 검색어 생성
     * 4. Naver Search API 제목 관련 링크 생성
     */
    // TODO 현재 코드에서는 Task가 올바르게 구성됐다고 보장할 수는 없다.
    public Long requestAnalyze(AnalyzeRequest request, JobType jobType) {
        Long jobId;
        if (jobType == JobType.Question) {
            jobId = jobCommandService.createQuestionJob("Job:%s".formatted(request.getQuery()));
        } else if (jobType == JobType.Memo) {
            jobId = jobCommandService.createMemoJob("Job:%s".formatted(request.getQuery()));
        } else {
            throw new RuntimeException("Invalid JobType");
        }

        // diffBotTask가 있다면
        var diffBotTask = taskQueryService.getTaskByTaskType(jobId, TaskType.DiffBot);
        diffBotTask.ifPresentOrElse(taskRequestDto -> {
                    taskCommandService.setInput(taskRequestDto.getTaskId(), Map.of("query", request.getQuery()));
                    eventPublisher.publishEvent(new TaskStartEvent(jobId, taskRequestDto.getTaskId()));
                },
                () -> {
                    taskQueryService.getTaskByTaskType(jobId, TaskType.HelpyV)
                            .ifPresent(helpyVTask -> {
                                        taskCommandService.setInput(helpyVTask.getTaskId(), Map.of("query", request.getQuery()));
                                        eventPublisher.publishEvent(new TaskStartEvent(jobId, helpyVTask.getTaskId()));
                                    }
                            );
                });

        return jobId;
    }
    
}
