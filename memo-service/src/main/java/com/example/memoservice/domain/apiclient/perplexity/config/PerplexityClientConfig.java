package com.example.memoservice.domain.apiclient.perplexity.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class PerplexityClientConfig {

    @Value("${perplexity.client.key}")
    private String perplexityClientKey;

    @Bean
    public RequestInterceptor perplexityRequestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer %s".formatted(perplexityClientKey));
            requestTemplate.header("Content-Type", "application/json");
            requestTemplate.header("Accept", "application/json");
        };
    }


}
