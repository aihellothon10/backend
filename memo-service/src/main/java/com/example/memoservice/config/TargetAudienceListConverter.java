//package com.example.memoservice.config;
//
//
//import com.example.memoservice.domain.memo.model.TargetAudience;
//import jakarta.persistence.AttributeConverter;
//import jakarta.persistence.Converter;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Converter
//public class TargetAudienceListConverter implements AttributeConverter<List<TargetAudience>, String> {
//
//    private static final String SPLIT_CHAR = ";";
//
//    @Override
//    public String convertToDatabaseColumn(List<TargetAudience> targetAudienceList) {
//        if (targetAudienceList == null || targetAudienceList.isEmpty()) {
//            return "";
//        }
//        return targetAudienceList.stream()
//                .map(TargetAudience::name)
//                .collect(Collectors.joining(SPLIT_CHAR));
//    }
//
//    @Override
//    public List<TargetAudience> convertToEntityAttribute(String dbData) {
//        if (dbData == null || dbData.isEmpty()) {
//            return List.of();
//        }
//        return Arrays.stream(dbData.split(SPLIT_CHAR))
//                .map(TargetAudience::valueOf)
//                .collect(Collectors.toList());
//    }
//}