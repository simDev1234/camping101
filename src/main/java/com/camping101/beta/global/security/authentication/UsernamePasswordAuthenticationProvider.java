package com.camping101.beta.global.security.authentication;

import com.camping101.beta.web.domain.member.service.signin.MemberSignInService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RequiredArgsConstructor
@Slf4j
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final MemberSignInService memberSignInService;

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {

        String email = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());

        MemberDetails memberDetails = memberSignInService.loadUserByUsername(email);

        if (!memberSignInService.isPasswordMatching(memberDetails.getMemberId(), password, memberDetails.getPassword())) {
            log.info("UsernamePasswordAuthenticationProvider.authenticate : 비밀번호가 일치하지 않습니다.");
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        if (!memberDetails.isEnabled()) {
            log.info("UsernamePasswordAuthenticationProvider.authenticate : 비활성화된 회원입니다.");
            throw new DisabledException("비활성화된 회원입니다.");
        }

        if (!memberDetails.isAccountNonLocked()) {
            log.info("UsernamePasswordAuthenticationProvider.authenticate : 정지된 회원입니다.");
            throw new LockedException("정지된 회원입니다.");
        }

        if (!memberDetails.isAccountNonExpired()) {
            log.info("UsernamePasswordAuthenticationProvider.authenticate : 탈퇴한 회원입니다.");
            throw new AccountExpiredException("탈퇴한 회원입니다.");
        }

        return new UsernamePasswordAuthenticationToken(memberDetails, null,
            memberDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
