package com.camping101.beta.web.domain.member.service.signin;

import com.camping101.beta.global.security.authentication.MemberDetails;
import com.camping101.beta.web.domain.member.dto.signin.SignInByEmailRequest;
import com.camping101.beta.web.domain.member.dto.token.ReissueRefreshTokenResponse;
import com.camping101.beta.web.domain.member.dto.token.TokenInfo;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberSignInService extends UserDetailsService {

    TokenInfo signInByEmail(SignInByEmailRequest request);

    MemberDetails loadUserByUsername(String username);

    boolean isPasswordMatching(Long memberId, String rawPassword, String encodedPassword);

    ReissueRefreshTokenResponse reissueAccessTokenByRefreshToken(String serverRefreshToken);

}
