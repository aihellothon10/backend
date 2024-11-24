package com.example.memoservice.domain.chat.model;

import com.example.memoservice.config.ContentConverter;
import com.example.memoservice.domain.apiclient.client.dto.Role;
import com.example.memoservice.domain.apiclient.elice.HelpyVChatRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class HelpVMessage {
    @Enumerated(EnumType.STRING)
    private Role role;

    @Convert(converter = ContentConverter.class)
    @Column(columnDefinition = "TEXT") // JSON 데이터 저장
    private List<Content> content;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Content {
        private String text;
        private HelpyVChatRequest.ImageUrl image_url;
        private String type;

        public static Content ofText(String text) {
            Content content = new Content();
            content.setText(text);
            content.setType("text");
            return content;
        }

        // TODO 이미지는 객체타입으로 입력됨...
        public static Content ofImage(String imgUrl) {
            Content content = new Content();
            content.setImage_url(new HelpyVChatRequest.ImageUrl(imgUrl));
            content.setType("image_url");
            return content;
        }

        public HelpyVChatRequest.Content toRequestContent() {
            return new HelpyVChatRequest.Content(this.text, this.image_url, this.type);
        }

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageUrl {
        private String url;
    }

    public static HelpVMessage ofSystem(List<Content> contents) {
        HelpVMessage message = new HelpVMessage();
        message.setRole(Role.system);
        message.setContent(contents);
        return message;
    }

    public static HelpVMessage ofUser(List<Content> contents) {
        HelpVMessage message = new HelpVMessage();
        message.setRole(Role.user);
        message.setContent(contents);
        return message;
    }

    public static HelpVMessage ofAssistant(List<Content> contents) {
        HelpVMessage message = new HelpVMessage();
        message.setRole(Role.assistant);
        message.setContent(contents);
        return message;
    }

}
