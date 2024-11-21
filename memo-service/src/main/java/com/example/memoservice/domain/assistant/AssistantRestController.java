package com.example.memoservice.domain.assistant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assistants")
@Slf4j
public class AssistantRestController {

    private final ChatClient chatClient;

    @GetMapping("/query")
    String query(@RequestParam String q) {
        var content = chatClient
                .prompt()
                .user(q)
                .call()
                .content();

        log.info("question     : [{}]", q);
        log.info("answer       : [{}]", content);
        return content;
    }


}
