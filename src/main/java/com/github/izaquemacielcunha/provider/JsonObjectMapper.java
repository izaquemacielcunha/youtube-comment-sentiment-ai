package com.github.izaquemacielcunha.provider;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.javalin.json.JavalinJackson;
import io.javalin.json.JsonMapper;

public class JsonObjectMapper {
    private static final JsonMapper objectMapper = new JavalinJackson().updateMapper(
            mapper -> {
                mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            });

    public static JsonMapper getMapper() {
        return objectMapper;
    }
}