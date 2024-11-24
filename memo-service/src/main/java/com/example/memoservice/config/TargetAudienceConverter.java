package com.example.memoservice.config;
//
//import com.example.memoservice.domain.memo.model.TargetAudience;
//import jakarta.persistence.AttributeConverter;
//import jakarta.persistence.Converter;
//
//@Converter
//public class TargetAudienceConverter implements AttributeConverter<TargetAudience, String> {
//
//    @Override
//    public String convertToDatabaseColumn(TargetAudience targetAudience) {
//        return targetAudience != null ? targetAudience.name() : null;
//    }
//
//    @Override
//    public TargetAudience convertToEntityAttribute(String dbData) {
//        if (dbData == null || dbData.isEmpty()) {
//            return null;
//        }
//        try {
//            return TargetAudience.valueOf(dbData);
//        } catch (IllegalArgumentException e) {
//            throw new IllegalStateException("Invalid value for TargetAudience: " + dbData, e);
//        }
//    }
//}