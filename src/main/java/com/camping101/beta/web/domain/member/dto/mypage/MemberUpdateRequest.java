package com.camping101.beta.web.domain.member.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
public class MemberUpdateRequest {

    private MultipartFile profileImage;
    private String nickname;
    private String phoneNumber;
    private String password;

}
