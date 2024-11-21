package com.example.memoservice.domain.apiclient.naversearch.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class NaverSearchApiClientConfig {

    @Value("${naver.client.client-id}")
    private String clientId;

    @Value("${naver.client.client-secret}")
    private String clientSecret;

    @Bean
    public RequestInterceptor naverSearchRequestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("X-Naver-Client-Id", clientId);
            requestTemplate.header("X-Naver-Client-Secret", clientSecret);
            requestTemplate.header("Content-Type", "application/json");
            requestTemplate.header("Accept", "application/json");
        };
    }


}
