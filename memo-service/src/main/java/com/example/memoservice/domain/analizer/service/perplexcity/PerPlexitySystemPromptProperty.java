package com.example.memoservice.domain.analizer.service.perplexcity;


import com.example.memoservice.domain.analizer.service.helpyv.HelpyVSystemPromptProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "perplexity.client.chat")
public class PerPlexitySystemPromptProperty {

    private List<HelpyVSystemPromptProperty.Prompt> systemPrompt;

    @Getter
    @Setter
    public static class Prompt {
        private String key;
        private String value;
    }
}
