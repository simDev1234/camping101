package com.camping101.beta.web.domain.member.dto.signup;

import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.db.entity.member.status.MemberStatus;
import com.camping101.beta.db.entity.member.type.MemberType;
import com.camping101.beta.db.entity.member.type.SignUpType;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SignUpByEmailRequest {

    private MultipartFile profileImage;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    private String nickname;
    private String phoneNumber;
    private MemberType memberType;

    public Member toNotActivatedMember(String profileImagePath, String encPassword) {
        return Member.builder()
            .profileImagePath(profileImagePath)
            .email(this.email)
            .password(encPassword)
            .phoneNumber(this.phoneNumber)
            .nickname(this.nickname)
            .signUpType(SignUpType.EMAIL)
            .memberType(this.memberType)
            .memberStatus(MemberStatus.NOT_ACTIVATED)
            .build();
    }

}
