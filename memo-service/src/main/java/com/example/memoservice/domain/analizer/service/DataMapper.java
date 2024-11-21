package com.example.memoservice.domain.analizer.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class DataMapper {

    public static Map<String, Object> toMapWithMapper(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(object, new TypeReference<Map<String, Object>>() {
        });
    }

    public static <T> T toObjectWithMapper(Map<?, ?> map, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(map, clazz);
    }

}
