package com.example.commonmodule.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JsonUtil {

    private static ObjectMapper objectMapper;

    @Autowired
    public JsonUtil(ObjectMapper objectMapper) {
        JsonUtil.objectMapper = objectMapper;
    }

    public static String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
