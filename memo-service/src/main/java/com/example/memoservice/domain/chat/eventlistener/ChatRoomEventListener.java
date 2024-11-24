package com.example.memoservice.domain.chat.eventlistener;

import com.example.memoservice.domain.analizer.event.JobCompletedEvent;
import com.example.memoservice.domain.chat.ChatCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChatRoomEventListener {

    private final ChatCommandService chatCommandService;

    @ApplicationModuleListener
    void jobCompleted(JobCompletedEvent event) {
        log.info("jobCompleted in ChatRoomEventListener [{}]", event);
        chatCommandService.addAssistantMessage(event.jobId());
    }

}
