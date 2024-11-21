package com.example.memoservice.domain.apiclient.perplexity;

import com.example.memoservice.domain.apiclient.perplexity.config.PerplexityClientConfig;
import com.example.memoservice.domain.apiclient.perplexity.dto.PerplexityRequest;
import com.example.memoservice.domain.apiclient.perplexity.dto.PerplexityResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "perplexity-client", url = "${perplexity.client.chat.url}", configuration = PerplexityClientConfig.class)
public interface PerplexityClient {

    @PostMapping
    PerplexityResponse chat(@RequestBody PerplexityRequest request);

}
