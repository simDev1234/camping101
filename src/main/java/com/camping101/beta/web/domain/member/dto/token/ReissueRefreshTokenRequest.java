package com.camping101.beta.web.domain.member.dto.token;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public class ReissueRefreshTokenRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    private String refreshToken;

}
