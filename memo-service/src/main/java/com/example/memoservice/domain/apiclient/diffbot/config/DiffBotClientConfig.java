package com.example.memoservice.domain.apiclient.diffbot.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class DiffBotClientConfig {

    @Value("${diffbot.client.key}")
    private String diffbotClientKey;

    @Bean
    public RequestInterceptor diffbotRequestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer %s".formatted(diffbotClientKey));
            requestTemplate.query("token", diffbotClientKey);
        };
    }

}
