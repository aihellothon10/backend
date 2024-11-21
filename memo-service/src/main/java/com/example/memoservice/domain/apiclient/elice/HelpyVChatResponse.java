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
        private Long prompt_tokens;
        private Long completion_tokens;
        private Long total_tokens;
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
        @JsonProperty("question_title")
        private String question_title;

        @JsonProperty("core_content_summary")
        private String coreContentSummary;

        @JsonProperty("content_contain_boolean")
        private int contentContainBoolean;

        @JsonProperty("content_contain_boolean_explain")
        private String contentContainBooleanExplain;

        @JsonProperty("answer")
        private String answer;

        @JsonProperty("books")
        private List<Book> books;
    }

    @Data
    public static class Book {
        //        @JsonProperty("title")
        private String title;
        //        @JsonProperty("authors")
        private String authors;
        //        @JsonProperty("year")
        private String year;

//        // 기본 생성자
//        public Book() {
//        }
//
//        @JsonCreator
//        public Book(
//                @JsonProperty("title") String title,
//                @JsonProperty("authors") String authors,
//                @JsonProperty("year") String year
//        ) {
//            this.title = title;
//            this.authors = authors;
//            this.year = year;
//        }
    }

}
