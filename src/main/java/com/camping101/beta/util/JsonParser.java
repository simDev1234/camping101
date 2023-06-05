package com.camping101.beta.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

@Component
@RequiredArgsConstructor
public class JsonParser {

    private final ObjectMapper objectMapper;

    public <T> T parseJsonToObject(String jsonString, Class<T> classType)
        throws JsonProcessingException {
        return objectMapper.readValue(jsonString, classType);
    }

    public <T> T parseJsonPayloadToObject(String jsonString, Class<T> classType)
        throws JsonProcessingException {

        String payload = jsonString.split("\\.")[1];
        String decodedPayload = new String(Base64Utils.decodeFromUrlSafeString(payload));

        return parseJsonToObject(decodedPayload, classType);
    }

}
