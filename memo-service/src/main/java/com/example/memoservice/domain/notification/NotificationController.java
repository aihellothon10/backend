package com.example.memoservice.domain.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/subscribe/jobs/{jobId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeJobs(@PathVariable Long jobId) {
        return notificationService.subscribe(1L);
    }

}
