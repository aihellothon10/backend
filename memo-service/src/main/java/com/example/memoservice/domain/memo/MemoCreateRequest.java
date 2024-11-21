package com.example.memoservice.domain.memo;

import com.example.memoservice.domain.memo.model.EvaluationStatus;
import com.example.memoservice.domain.memo.model.TargetAudience;

public record MemoCreateRequest(String title,
                                String content,
                                EvaluationStatus evaluationStatus,
                                TargetAudience targetAudience,
                                String userInput,
                                String analysisSummary,
                                String source) {
}
