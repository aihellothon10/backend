package com.example.memoservice.domain.apiclient.diffbot;

import com.example.memoservice.domain.apiclient.diffbot.config.DiffBotClientConfig;
import com.example.memoservice.domain.apiclient.diffbot.dto.DiffbotResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "diff-bot", url = "${diffbot.client.crawling.url}", configuration = DiffBotClientConfig.class)
public interface DiffbotApiClient {

    @GetMapping
    DiffbotResponse crawlPage(@RequestParam String url);

}
