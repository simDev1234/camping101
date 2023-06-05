package com.camping101.beta.web.domain.member.dto.token;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public class ReissueRefreshTokenResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String accessToken;

}
