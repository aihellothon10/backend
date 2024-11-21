package com.example.memoservice.domain.analizer.eventlistener;

import com.example.memoservice.domain.analizer.JobCommandService;
import com.example.memoservice.domain.analizer.event.JobCompletedEvent;
import com.example.memoservice.domain.analizer.event.JobCreatedEvent;
import com.example.memoservice.domain.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

/**
 * Job 생성 후 각 Task를 실행 이벤트
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JobEventListener {

    private final JobCommandService jobCommandService;

    private final ApplicationEventPublisher eventPublisher;

    private final NotificationService notificationService;

    record NotificationData(Long jobId, String message) {
    }

    @ApplicationModuleListener
    void jobCreated(JobCreatedEvent event) {
        log.info("jobCreated in JobEventListener [{}]", event);
        notificationService.notify(1L, new TaskEventListener.NotificationData(event.jobId(), "Job Start"), "Good Job", "job");

        jobCommandService.completeJob(event.jobId());
    }

    @ApplicationModuleListener
    void jobCompleted(JobCompletedEvent event) {
        log.info("jobCompleted in JobEventListener [{}]", event);
        notificationService.notify(1L, new TaskEventListener.NotificationData(event.jobId(), "Job End"), "Good Job", "job");

    }

}
