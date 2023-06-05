package com.camping101.beta.web.domain.member.dto.signin;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;

import com.camping101.beta.db.entity.member.type.MemberType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SignInByEmailRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    private String email;
    @NotBlank
    private String password;

    @ApiModelProperty(hidden = true)
    private MemberType memberType;



}
