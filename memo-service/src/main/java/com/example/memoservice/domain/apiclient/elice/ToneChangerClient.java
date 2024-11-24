package com.example.memoservice.domain.apiclient.elice;

import com.example.memoservice.domain.apiclient.elice.config.EliceConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "tone-changer", url = "${elice.client.api.tone-changer.url}", configuration = EliceConfig.class)
public interface ToneChangerClient {
    @PostMapping
    String changeTone(@RequestBody ToneChangerDto dto);
}
