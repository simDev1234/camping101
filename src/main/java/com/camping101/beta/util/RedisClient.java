package com.camping101.beta.util;

import com.amazonaws.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisClient {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;

    public <T> boolean put(String key, T data) {
        try {
            String value = objectMapper.writeValueAsString(data);
            log.info("RedisClient.put : {}", value);
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            valueOperations.set(parseToUTF8String(key), parseToUTF8String(value));
            return true;
        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(x -> log.warn(x.toString()));
            return false;
        }
    }

    public <T> boolean put(String key, T data, long expiredSeconds) {
        try {
            String value = objectMapper.writeValueAsString(data);
            log.info("RedisClient.put : {}", value);
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            valueOperations.set(parseToUTF8String(key), parseToUTF8String(value));
            redisTemplate.expire(parseToUTF8String(key), expiredSeconds, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(x -> log.warn(x.toString()));
            return false;
        }
    }

    public <T> Optional<T> get(String key, Class<T> classType) {
        try {
            String value = redisTemplate.opsForValue().get(parseToUTF8String(key));
            return StringUtils.isNullOrEmpty(value) ?
                Optional.empty() : Optional.of(objectMapper.readValue(value, classType));
        } catch (Exception e) {
            e.getStackTrace();
            Arrays.stream(e.getStackTrace()).forEach(x -> log.warn(x.toString()));
            return Optional.empty();
        }
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    private String parseToUTF8String(String s) {
        return new String(s.getBytes(), StandardCharsets.UTF_8);
    }

}
