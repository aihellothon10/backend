package com.example.memoservice.domain.apiclient.elice;

import lombok.Data;

@Data
public class ToxicityPredictionResponse {
    private boolean isToxic;
    private float score;
}
