package com.camping101.beta.web.domain.member.dto.mypage;

import com.camping101.beta.db.entity.member.Member;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberInfoResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long memberId;
    private String image;
    private String email;
    private String nickname;
    private String phoneNumber;
    private String memberType;
    private String signUpType;

    public static MemberInfoResponse fromEntity(Member member) {
        return MemberInfoResponse.builder()
            .memberId(member.getMemberId())
            .image(member.getProfileImagePath())
            .email(member.getEmail())
            .nickname(member.getNickname())
            .phoneNumber(member.getPhoneNumber())
            .memberType(member.getMemberType().name())
            .signUpType(member.getSignUpType().name())
            .build();
    }
}
