package com.example.memoservice.domain.apiclient.elice.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class EliceConfig {

    @Value("${elice.client.key}")
    private String eliceClientKey;

    @Bean
    public RequestInterceptor eliceRequestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer %s".formatted(eliceClientKey));
        };
    }

}
