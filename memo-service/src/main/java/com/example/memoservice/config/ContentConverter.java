package com.example.memoservice.config;

import com.example.memoservice.domain.chat.model.HelpVMessage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

@Converter
public class ContentConverter implements AttributeConverter<List<HelpVMessage.Content>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<HelpVMessage.Content> attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(attribute); // List<Content> -> JSON
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert content list to JSON", e);
        }
    }

    @Override
    public List<HelpVMessage.Content> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<HelpVMessage.Content>>() {
            }); // JSON -> List<Content>
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert JSON to content list", e);
        }
    }
}