package com.example.memoservice.domain.apiclient.elice;

import com.example.memoservice.config.ContentConverter;
import com.example.memoservice.domain.apiclient.client.dto.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class HelpyVChatRequest {

    private String model;

    List<MessageDto> messages;

    public static HelpyVChatRequest of(List<MessageDto> messages) {
        return new HelpyVChatRequest("helpy-v-large", messages);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageDto {
        @Enumerated(EnumType.STRING)
        private Role role;

        @Convert(converter = ContentConverter.class)
        @Column(columnDefinition = "TEXT") // JSON 데이터 저장
        private List<Content> content;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Content {
        private String text;
        private ImageUrl image_url;
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
            content.setImage_url(new ImageUrl(imgUrl));
            content.setType("image_url");
            return content;
        }

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageUrl {
        private String url;
    }

    public static MessageDto ofSystem(List<Content> contents) {
        MessageDto message = new MessageDto();
        message.setRole(Role.system);
        message.setContent(contents);
        return message;
    }

    public static MessageDto ofUser(List<Content> contents) {
        MessageDto message = new MessageDto();
        message.setRole(Role.user);
        message.setContent(contents);
        return message;
    }

    public static MessageDto ofAssistant(List<Content> contents) {
        MessageDto message = new MessageDto();
        message.setRole(Role.assistant);
        message.setContent(contents);
        return message;
    }

}

//public record HelpyVChatRequest(
//        String model,
//        List<MessageDto> messages) {
//
//    public static HelpyVChatRequest of(List<MessageDto> messages) {
//        return new HelpyVChatRequest("helpy-v-large", messages);
//    }
//
//    public record MessageDto(
//            Role role,
//            List<Content> content) {
//    }
//
//    public record Content(
//            String type,
//            String text) {
//
//        public static Content of(String text) {
//            return new Content("text", text);
//        }
//
//    }
//
//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class ImageUrl {
//        private String url;
//    }
//
//}