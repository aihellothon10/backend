package com.example.memoservice.domain.apiclient.naversearch;

import com.example.memoservice.domain.apiclient.naversearch.config.NaverSearchApiClientConfig;
import com.example.memoservice.domain.apiclient.naversearch.dto.NaverSearchResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "naver-search", url = "${naver.client.search.url}", configuration = NaverSearchApiClientConfig.class)
public interface NaverSearchClient {

    @GetMapping
    NaverSearchResult search(@RequestParam String query,
                             @RequestParam(required = false, defaultValue = "1") int start,
                             @RequestParam(required = false, defaultValue = "10") int display);

}
