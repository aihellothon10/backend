package com.example.memoservice.domain.analizer.eventlistener;

import com.example.memoservice.domain.analizer.TaskCommandService;
import com.example.memoservice.domain.analizer.TaskQueryService;
import com.example.memoservice.domain.analizer.event.JobCompletedEvent;
import com.example.memoservice.domain.analizer.event.TaskStartEvent;
import com.example.memoservice.domain.analizer.service.AnalyzerFactoryService;
import com.example.memoservice.domain.analizer.service.helpyv.HelpyVService;
import com.example.memoservice.domain.analizer.service.perplexcity.PerPlexityService;
import com.example.memoservice.domain.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskEventListener {

    private final TaskCommandService taskCommandService;
    private final TaskQueryService taskQueryService;

    private final PerPlexityService perplexityService;
    private final HelpyVService helpyVService;

    // API 호출 서비스
    private final AnalyzerFactoryService analyzerFactoryService;

    private final NotificationService notificationService;

    private final ApplicationEventPublisher eventPublisher;

    record NotificationData(Long taskId, String message) {
    }

    // TODO 분석 결과 후처리 필요
    // TODO Task 상태와 입/출력 데이터 관리가 파편화됨
    @ApplicationModuleListener
    @Async
    void startTask(TaskStartEvent event) {
        log.info("taskStart in TaskEventListener [{}]", event);
        notificationService.notify(1L, new NotificationData(event.taskId(), "Task Start"), "Task Set", "task");

        if (event.taskId() == null) {
            log.error("taskId is null");
            return;
        }

        Long nextTaskId = analyzerFactoryService.process(event.taskId());
        if (nextTaskId != null) {
            eventPublisher.publishEvent(new TaskStartEvent(event.jobId(), nextTaskId));
        }

        notificationService.notify(1L, new NotificationData(event.taskId(), "Task Done"), "Task Set", "task");

        // 해당 Job의 모든 Task가 완료되었는지 확인
        if (taskQueryService.isCompleted(event.jobId())) {
            eventPublisher.publishEvent(new JobCompletedEvent(event.jobId()));
        }
    }

}
