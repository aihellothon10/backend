package com.example.memoservice.domain.apiclient.elice;

import com.example.memoservice.domain.apiclient.elice.config.EliceConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "toxicity-prediction", url = "${elice.client.api.toxicity-prediction.url}", configuration = EliceConfig.class)
public interface ToxicityPredictionClient {

    @PostMapping
    List<ToxicityPredictionResponse> check(@RequestBody ToxicityPredictionRequest request);

}
