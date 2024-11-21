package com.example.memoservice.domain.apiclient.perplexity.dto;

import com.example.memoservice.domain.apiclient.client.dto.Role;

import java.util.List;

public record PerplexityResponse(String id,
                                 String model,
                                 Long created,
                                 Usage usage,
                                 List<String> citations,
                                 String object,
                                 List<Choice> choices) {

    public record Usage(Long prompt_tokens,
                        Long completion_tokens,
                        Long total_tokens) {
    }

    public record Choice(Long index,
                         String finish_reason,
                         Message message,
                         Message delta) {
    }

    public record Message(
            Role role,
            String content) {
    }
}
