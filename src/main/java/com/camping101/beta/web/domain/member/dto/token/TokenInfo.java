package com.camping101.beta.web.domain.member.dto.token;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TokenInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String accessToken;
    private String refreshToken;

    public static TokenInfo of(String rawAccessToken, String rawRefreshToken) {
        return TokenInfo.builder()
            .accessToken(String.format("Bearer %s", rawAccessToken))
            .refreshToken(String.format("Basic %s", rawRefreshToken))
            .build();
    }

}
