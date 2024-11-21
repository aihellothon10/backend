package com.example.memoservice.domain.apiclient.perplexity.dto;


import com.example.memoservice.domain.apiclient.client.dto.Role;

import java.util.List;

public record PerplexityRequest(String model,
                                List<Message> messages) {

    public static PerplexityRequest of(List<Message> messages) {
        return new PerplexityRequest("llama-3.1-sonar-small-128k-online", messages);
    }

    public record Message(Role role,
                          String content) {

        public static Message of(Role role, String content) {
            return new Message(role, content);
        }
    }
}
