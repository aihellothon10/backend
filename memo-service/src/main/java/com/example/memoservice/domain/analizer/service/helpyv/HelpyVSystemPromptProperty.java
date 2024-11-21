package com.example.memoservice.domain.analizer.service.helpyv;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "elice.client.api.helpy-vchat")
public class HelpyVSystemPromptProperty {

    private List<Prompt> systemPrompt;

    @Getter
    @Setter
    public static class Prompt {
        private String key;
        private String value;
    }
}
