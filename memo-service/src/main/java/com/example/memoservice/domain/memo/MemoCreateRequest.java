package com.example.memoservice.domain.memo;

import java.util.List;

public record MemoCreateRequest(Long jobId,
                                List<String> targetBabies,
                                List<String> targetMembers,
                                List<String> targetAudiences) {
}
