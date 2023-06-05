package com.camping101.beta.global.security.filter;

import com.camping101.beta.global.security.authentication.MemberDetails;
import com.camping101.beta.util.FilterResponseHandler;
import com.camping101.beta.web.domain.member.service.token.TokenService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {

        log.info("JwtAuthenticationFilter 접근 경로 : {}", request.getServletPath());

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        log.info("JwtAuthenticationFilter.attemptAuthentication : 로그인 시도 >> email : {}, pw : {}",
            email, password);

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);

        return authenticationManager.authenticate(authentication);

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain,
        Authentication authentication) throws IOException {

        log.info(
            "JwtAuthenticationFilter.successAuthentication : 인증 성공. Access Token, Refresh Token 발급.");

        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        String accessToken = tokenService.createAccessToken(memberDetails);
        String refreshToken = tokenService.createRefreshToken(memberDetails);
        tokenService.setTokenHeader(response, accessToken, refreshToken);
        FilterResponseHandler.sendSuccessResponse(response, "로그인 성공");

    }

}
