package com.example.memoservice.domain.apiclient.elice;

import com.example.memoservice.domain.apiclient.client.dto.Role;

import java.util.List;

public record HelpyVChatRequest(
        String model,
        List<MessageDto> messages) {

    public static HelpyVChatRequest of(List<MessageDto> messages) {
        return new HelpyVChatRequest("helpy-v-large", messages);
    }

    public record MessageDto(
            Role role,
            List<Content> content) {
    }

    public record Content(
            String type,
            String text) {

        public static Content of(String text) {
            return new Content("text", text);
        }

    }

}