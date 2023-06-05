package com.camping101.beta.web.domain.member.service.signin.oAuth;

import com.camping101.beta.web.domain.member.dto.token.TokenInfo;

public interface OAuthService {

    TokenInfo signInByOAuth(String code);

    String reissueAccessTokenByRefreshToken(String previousAccessToken);

    void revokeAccessTokenForLogOut(String authorization);

}
