package com.example.memoservice.domain.apiclient.elice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToxicityPredictionRequest {
    List<String> text;
}
