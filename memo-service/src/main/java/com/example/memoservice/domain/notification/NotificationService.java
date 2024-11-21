package com.example.memoservice.domain.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final EmitterRepository emitterRepository;
    
    private static final Long DEFAULT_TIMEOUT = 600L * 1000 * 60;
    private static final String DEFAULT_CHANNEL = "sse";

    public SseEmitter subscribe(Long jobId) {
        SseEmitter emitter = createEmitter(jobId);

        notify(jobId, "EventStream Created. :%d".formatted(jobId), "connected");
        return emitter;
    }

    public <T> void notify(Long jobId, T data, String comment, String type) {
        sendToClient(jobId, data, comment, type);
    }

    public void notify(Long jobId, Object data, String comment) {
        sendToClient(jobId, data, comment, DEFAULT_CHANNEL);
    }

    private <T> void sendToClient(Long jobId, T data, String comment, String type) {
        SseEmitter emitter = emitterRepository.get(jobId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .id(String.valueOf(jobId))
                        .name(type)
                        .data(data)
                        .comment(comment));
            } catch (IOException e) {
                emitterRepository.deleteById(jobId);
                emitter.completeWithError(e);
            }
        }
    }

    private SseEmitter createEmitter(Long jobId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(jobId, emitter);

        emitter.onCompletion(() -> emitterRepository.deleteById(jobId));
        emitter.onTimeout(() -> emitterRepository.deleteById(jobId));

        return emitter;
    }

}