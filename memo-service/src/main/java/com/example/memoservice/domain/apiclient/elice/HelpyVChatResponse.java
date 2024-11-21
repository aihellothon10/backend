package com.example.memoservice.domain.apiclient.elice;

import com.example.memoservice.domain.apiclient.client.dto.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class HelpyVChatResponse {
    private String id;
    private String object;
    private double created;
    private String model;
    private List<Choice> choices;
    private Usage usage;

    @Data
    public static class Choice {
        private Long index;
        private Message message;
        private String finish_reason;
    }

    @Data
    public static class Usage {
        private Long promptTokens;
        private Long completionTokens;
        private Long totalTokens;
    }

    @Data
    public static class Message {
        private Role role;
        private Content content;

        public Message(@JsonProperty("role") Role role,
                       @JsonProperty("content") String content) {
            this.role = role;

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                this.content = objectMapper.readValue(content, Content.class);
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse content", e);
            }
        }


    }

    @Data
    public static class Content {
        private String questionTitle;
        private String coreContentSummary;
        private int contentContainBoolean;
        private String contentContainBooleanExplain;
        private String answer;
        private List<Book> books;
    }

    @Data
    public static class Book {
        private String title;
        private String authors;
        private String year;
    }

}
