package com.example.memoservice.config;

import com.example.memoservice.domain.apiclient.elice.ToxicityPredictionResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.List;


@Converter
public class ToxicityPredictionResponseListConverter implements AttributeConverter<List<ToxicityPredictionResponse>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<ToxicityPredictionResponse> attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert list to JSON string", e);
        }
    }

    @Override
    public List<ToxicityPredictionResponse> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<ToxicityPredictionResponse>>() {
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert JSON string to list", e);
        }
    }
}