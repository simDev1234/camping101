package com.camping101.beta.db.entity.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "temporalPassword", timeToLive = 60L * 5) // 5분
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TemporalPassword {

    @Id
    private String temporalPassword;
    private Long memberId;

}
