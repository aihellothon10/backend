package com.example.memoservice.domain.analizer.service;

public interface AnalyzerService {

    // return next task id
    Long analyze(Long currentTaskId);

    boolean hasPrompt();

    String getSystemPrompt();

}
