package com.camping101.beta.web.domain.member.controller;

import com.camping101.beta.db.entity.member.type.MemberType;
import com.camping101.beta.web.domain.member.dto.mypage.TemporalPasswordSendRequest;
import com.camping101.beta.web.domain.member.dto.signin.SignInByEmailRequest;
import com.camping101.beta.web.domain.member.dto.token.ReissueRefreshTokenRequest;
import com.camping101.beta.web.domain.member.dto.token.ReissueRefreshTokenResponse;
import com.camping101.beta.web.domain.member.dto.token.TokenInfo;
import com.camping101.beta.web.domain.member.service.signin.MemberSignInService;
import com.camping101.beta.web.domain.member.service.signin.oAuth.OAuthService;
import com.camping101.beta.web.domain.member.service.temporalPassword.TemporalPasswordService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

import static com.camping101.beta.global.config.GoogleOAuthConfig.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/signin")
@Api(tags = {"캠핑 101 - 로그인 API"})
@Slf4j
public class MemberSignInController {

    private final MemberSignInService memberSignInService;
    private final OAuthService googleOAuthService;
    private final TemporalPasswordService temporalPasswordService;

    @PostMapping("/temporal-password")
    public ResponseEntity<Void> temporalPasswordSend(
        @RequestBody TemporalPasswordSendRequest request) {

        temporalPasswordService.sendTemporalPassword(request.getEmail());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/mail")
    public ResponseEntity<Void> emailSignIn(@RequestBody SignInByEmailRequest request,
                                            @ApiIgnore HttpServletResponse response) {

        request.setMemberType(MemberType.CUSTOMER);

        TokenInfo tokenInfo = memberSignInService.signInByEmail(request);

        response.setHeader("access-token", tokenInfo.getAccessToken());
        response.setHeader("refresh-token", tokenInfo.getRefreshToken());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/google")
    public void googleSignIn(@ApiIgnore HttpServletResponse response) throws IOException {

        log.info("MemberSignInController.googleSignIn : 구글 인증 코드를 요청합니다.");

        String googleAuthCodeUri = String.format("%s?client_id=%s&redirect_uri=%s&scope=%s" +
                "&access_type=offline&response_type=code&approval_prompt=force&include_granted_scopes=true",
            googleAuthorizationUri, googleClientId, googleRedirectUri, googleScope);

        log.info(googleAuthCodeUri);

        response.sendRedirect(googleAuthCodeUri);
    }

    @GetMapping("/oauth/google")
    @ApiIgnore
    public ResponseEntity<TokenInfo> googleSignIn(@RequestParam String code,
        HttpServletResponse response) {

        log.info("MemberSignInController.googleSignIn : 구글 인증 코드 \"{}\"", code);

        TokenInfo tokenInfo = googleOAuthService.signInByOAuth(code);

        addAccessTokenAndRefreshTokenToResponseHeader(response, tokenInfo);

        return ResponseEntity.ok(tokenInfo);
    }

    private void addAccessTokenAndRefreshTokenToResponseHeader(HttpServletResponse response,
        TokenInfo tokenInfo) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setHeader("access-token", tokenInfo.getAccessToken());
        response.setHeader("refresh-token", tokenInfo.getRefreshToken());
    }

    @PostMapping("/refresh")
    public ResponseEntity<ReissueRefreshTokenResponse> refreshTokenReissue(
        @Valid @RequestBody ReissueRefreshTokenRequest reissueRefreshTokenRequest) {

        ReissueRefreshTokenResponse response = memberSignInService.reissueAccessTokenByRefreshToken(
            reissueRefreshTokenRequest.getRefreshToken());

        return ResponseEntity.ok(response);
    }

}
