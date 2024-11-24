package com.example.memoservice.domain.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

//    @GetMapping(value = "/subscribe/jobs/{jobId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public SseEmitter subscribeJobs(@PathVariable Long jobId) {
//        return notificationService.subscribe(jobId);
//    }

}
