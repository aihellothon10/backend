package com.example.memoservice.domain.apiclient.elice;

import com.example.memoservice.domain.apiclient.elice.config.EliceConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "helpy-vchat", url = "${elice.client.api.helpy-vchat.url}", configuration = EliceConfig.class)
public interface HelpyVChatClient {

    @PostMapping
    HelpyVChatResponse chat(@RequestBody HelpyVChatRequest request);

}
