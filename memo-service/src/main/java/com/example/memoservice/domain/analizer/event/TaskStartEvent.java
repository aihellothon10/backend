package com.example.memoservice.domain.analizer.event;

public record TaskStartEvent(Long jobId,
                             Long taskId) {
}
