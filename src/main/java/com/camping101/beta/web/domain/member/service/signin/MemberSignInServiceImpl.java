package com.camping101.beta.web.domain.member.service.signin;

import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.db.entity.member.RefreshToken;
import com.camping101.beta.db.entity.member.type.MemberType;
import com.camping101.beta.db.entity.member.type.SignUpType;
import com.camping101.beta.global.security.authentication.MemberDetails;
import com.camping101.beta.web.domain.member.dto.signin.SignInByEmailRequest;
import com.camping101.beta.web.domain.member.dto.token.ReissueRefreshTokenResponse;
import com.camping101.beta.web.domain.member.dto.token.TokenInfo;
import com.camping101.beta.web.domain.member.exception.MemberException;
import com.camping101.beta.web.domain.member.exception.TokenException;
import com.camping101.beta.web.domain.member.repository.MemberRepository;
import com.camping101.beta.web.domain.member.service.signin.oAuth.OAuthService;
import com.camping101.beta.web.domain.member.service.temporalPassword.TemporalPasswordService;
import com.camping101.beta.web.domain.member.service.token.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.lang.Strings;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.camping101.beta.web.domain.member.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberSignInServiceImpl implements MemberSignInService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TemporalPasswordService temporalPasswordService;
    private final OAuthService googleOAuthService;
    private final TokenService tokenService;

    @Override
    public TokenInfo signInByEmail(SignInByEmailRequest request) {

        Member member = memberRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        validateIfMemberTypeNotMatching(member, request.getMemberType());
        validateIfMemberSignedUpByEmail(member);
        validateIfPasswordMatching(request, member);

        String accessToken = tokenService.createAccessToken(member);
        String refreshToken = tokenService.createRefreshToken(member);

        return new TokenInfo(accessToken, refreshToken);
    }

    private static void validateIfMemberTypeNotMatching(Member member, MemberType memberType) {
        if (!memberType.equals(member.getMemberType())) {
            throw new MemberException(MEMBER_TYPE_NOT_MATCHING);
        }
    }

    private void validateIfMemberSignedUpByEmail(Member member) {

        if (!SignUpType.EMAIL.equals(member.getSignUpType())) {

            log.info("MemberSignInServiceImpl.validateIfMemberSignedUpByEmail: 이메일 회원이 아닙니다.");

            throw new MemberException(MEMBER_SIGN_IN_ERROR);
        }

    }

    private void validateIfPasswordMatching(SignInByEmailRequest request, Member member) {
        if (!isPasswordMatching(member.getMemberId(), request.getPassword(), member.getPassword())) {
            log.info("MemberSignInServiceImpl,signInByEmail : 비밀번호 불일치");
            throw new BadCredentialsException("비밀번호 불일치");
        }
    }

    @Override
    public MemberDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        return new MemberDetails(member);
    }

    @Override
    public boolean isPasswordMatching(Long memberId, String rawPassword, String encodedPassword) {

        if (temporalPasswordService.isTemporalPasswordMatching(rawPassword, memberId)) {
            log.info("MemberSignInServiceImpl.isPasswordMatching : 임시 비밀번호가 일치합니다.");
            return true;
        }

        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public ReissueRefreshTokenResponse reissueAccessTokenByRefreshToken(String refreshToken) {

        try {

            validateRefreshTokenFormat(refreshToken);

            RefreshToken redisRefreshToken = tokenService.findRefreshTokenById(refreshToken)
                .orElseThrow(() -> new TokenException(INVALID_REFRESH_TOKEN));

            validateIfRefreshTokenExpired(redisRefreshToken);

            String newAccessToken = isRefreshTokenCreatedByGoogleOAuth(redisRefreshToken) ?
                googleOAuthService.reissueAccessTokenByRefreshToken(
                    redisRefreshToken.getGoogleRefreshToken()) :
                tokenService.createAccessTokenByRefreshToken(refreshToken);

            return new ReissueRefreshTokenResponse(newAccessToken);

        } catch (ExpiredJwtException e) {

            log.info("MemberSignInServiceImpl.refreshToken : Refresh Token 만료");
            throw new MemberException(INVALID_REFRESH_TOKEN);

        } catch (MalformedJwtException | SignatureException | UnsupportedJwtException e) {

            log.info("MemberSignInServiceImpl.refreshToken : Refresh Token 파싱 중 오류 발생");
            throw new MemberException(INVALID_REFRESH_TOKEN);

        } catch (Exception e) {

            log.warn("MemberSignInServiceImp.refreshToken : 알 수 없는 오류");
            e.printStackTrace();
            throw new MemberException(INVALID_REFRESH_TOKEN);

        }

    }

    private void validateRefreshTokenFormat(String refreshToken) {

        if (!tokenService.isNotBlankAndStartsWithBearer(refreshToken)) {

            log.info("MemberSignInServiceImpl.validateRefreshTokenFormat : 리프래시 토큰 포맷이 잘못되었습니다.");

            throw new TokenException(INVALID_REFRESH_TOKEN);
        }

    }

    private void validateIfRefreshTokenExpired(RefreshToken redisRefreshTokenInfo) {

        if (LocalDateTime.now().isAfter(redisRefreshTokenInfo.getExpiredAt())) {

            log.info("MemberSignInServiceImpl.validateIfRefreshTokenExpired : 리프래시 토큰이 만료되었습니다.");

            throw new TokenException(INVALID_REFRESH_TOKEN);
        }

    }

    private boolean isRefreshTokenCreatedByGoogleOAuth(RefreshToken redisRefreshToken) {
        return Strings.hasText(redisRefreshToken.getGoogleRefreshToken());
    }

}
