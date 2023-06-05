package com.camping101.beta.web.domain.member.dto.signin.oAuth;

import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.db.entity.member.status.MemberStatus;
import com.camping101.beta.db.entity.member.type.MemberType;
import com.camping101.beta.db.entity.member.type.SignUpType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoogleAccountInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String email;
    private String name;
    private String givenName;
    private String familyName;
    private String picture;

    public Member toActivatedMember() {
        return Member.builder()
            .profileImagePath(this.picture)
            .email(this.email)
            .nickname(this.givenName)
            .signUpType(SignUpType.GOOGLE)
            .memberType(MemberType.CUSTOMER)
            .memberStatus(MemberStatus.IN_USE)
            .build();
    }

}
