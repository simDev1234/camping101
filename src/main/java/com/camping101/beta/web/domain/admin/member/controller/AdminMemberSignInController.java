package com.camping101.beta.web.domain.admin.member.controller;

import com.camping101.beta.db.entity.member.type.MemberType;
import com.camping101.beta.web.domain.member.dto.signin.SignInByEmailRequest;
import com.camping101.beta.web.domain.member.dto.token.TokenInfo;
import com.camping101.beta.web.domain.member.service.signin.MemberSignInService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/signin")
@Api(tags = {"관리자사이트 - 로그인 API"})
@Slf4j
public class AdminMemberSignInController {

    private final MemberSignInService memberSignInService;

    @PostMapping("/mail")
    public ResponseEntity<Void> emailSignIn(@RequestBody SignInByEmailRequest request,
                                            @ApiIgnore HttpServletResponse response) {

        request.setMemberType(MemberType.ADMIN);

        TokenInfo tokenInfo = memberSignInService.signInByEmail(request);

        response.setHeader("access-token", tokenInfo.getAccessToken());
        response.setHeader("refresh-token", tokenInfo.getRefreshToken());

        return ResponseEntity.ok().build();
    }

}
