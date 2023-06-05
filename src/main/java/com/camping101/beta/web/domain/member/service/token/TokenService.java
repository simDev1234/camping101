package com.camping101.beta.web.domain.member.service.token;

import static com.camping101.beta.db.entity.member.type.SignUpType.GOOGLE;

import com.camping101.beta.db.entity.member.AccessTokenBlackList;
import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.db.entity.member.RefreshToken;
import com.camping101.beta.db.entity.member.type.MemberType;
import com.camping101.beta.db.entity.member.type.SignUpType;
import com.camping101.beta.global.security.authentication.MemberDetails;
import com.camping101.beta.util.JwtProvider;
import com.camping101.beta.web.domain.member.repository.AccessTokenBlackListRepository;
import com.camping101.beta.web.domain.member.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Token Provider - Access Token 생성 - Refresh Token 생성 및 Redis 저장 (저장 시, Bearer는 빼고 저장한다.)
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AccessTokenBlackListRepository accessTokenBlackListRepository;

    @Value("${token.jwt.accesstoken}")
    private long accessExpireMillisecond;
    @Value("${token.jwt.refreshtoken}")
    private long refreshExpireMillisecond;

    private final static String TOKEN_PREFIX = "Bearer ";

    public Optional<RefreshToken> findRefreshTokenById(String refreshToken) {

        return refreshTokenRepository.findById(refreshToken.substring(TOKEN_PREFIX.length()));
    }

    public MemberDetails getMemberDetailsByAccessToken(String accessToken)
        throws MalformedJwtException, ExpiredJwtException, SignatureException, UnsupportedJwtException {

        Member member = extractMemberFromToken(accessToken);

        return new MemberDetails(member);
    }

    private Member extractMemberFromToken(String token)
        throws MalformedJwtException, ExpiredJwtException, SignatureException, UnsupportedJwtException {

        Claims claims = jwtProvider.getClaim(token.substring(TOKEN_PREFIX.length()));
        Long memberId = jwtProvider.getMemberId(claims);
        String email = jwtProvider.getEmail(claims);
        MemberType memberType = jwtProvider.getMemberType(claims);
        SignUpType signUpType = jwtProvider.getSignUpType(claims);

        Member member = Member.builder()
            .memberId(memberId)
            .email(email)
            .memberType(memberType)
            .signUpType(signUpType)
            .build();

        return member;

    }

    public void setTokenHeader(HttpServletResponse response,
        String accessToken, String refreshToken) {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setHeader("access-token", accessToken);
        response.setHeader("refresh-token", refreshToken);
    }

    public String createAccessToken(MemberDetails memberDetails) {

        return createAccessToken(memberDetails.getEmail(), memberDetails.getMemberId(),
            memberDetails.getMemberType(), memberDetails.getSignUpType());
    }

    public String createAccessToken(Member member) {

        String rawAccessToken = jwtProvider.createToken(member, accessExpireMillisecond);

        log.info("TokenProvider.createAccessToken : Bearer {}", rawAccessToken);

        return String.format("%s%s", TOKEN_PREFIX, rawAccessToken);
    }

    public String createAccessToken(String email, Long memberId, MemberType memberType,
        SignUpType signUpType) {

        String rawAccessToken = jwtProvider.createToken(email, memberId, memberType, signUpType,
            accessExpireMillisecond);

        log.info("TokenProvider.createAccessToken : Bearer {}", rawAccessToken);

        return String.format("%s%s", TOKEN_PREFIX, rawAccessToken);
    }

    public String createAccessTokenByRefreshToken(String refreshToken)
        throws MalformedJwtException, ExpiredJwtException, SignatureException, UnsupportedJwtException {

        Member member = extractMemberFromToken(refreshToken);

        return createAccessToken(member);
    }

    public String createRefreshToken(MemberDetails memberDetails) {

        return createRefreshToken(memberDetails.getEmail(), memberDetails.getMemberId(),
            memberDetails.getMemberType(), memberDetails.getSignUpType());
    }

    public String createRefreshToken(Member member) {

        return createRefreshToken(member.getEmail(), member.getMemberId(),
            member.getMemberType(), member.getSignUpType());
    }

    public String createRefreshToken(String email, Long memberId, MemberType memberType,
        SignUpType signUpType) {

        String rawRefreshToken = jwtProvider.createToken(email, memberId, memberType, signUpType,
            refreshExpireMillisecond);

        log.info("TokenProvider.createRefreshToken : Bearer {}", rawRefreshToken);

        RefreshToken refreshToken = RefreshToken.builder()
            .refreshToken(rawRefreshToken)
            .memberId(memberId)
            .expiredAt(LocalDateTime.now().plusSeconds(refreshExpireMillisecond / 1000))
            .build();

        refreshTokenRepository.save(refreshToken);

        return String.format("%s%s", TOKEN_PREFIX, rawRefreshToken);
    }

    public String createRefreshToken(String googleRefreshToken, Member member) {

        return createRefreshToken(googleRefreshToken, member.getEmail(), member.getMemberId(),
            member.getMemberType(), GOOGLE);
    }

    public String createRefreshToken(String googleRefreshToken, String email, Long memberId,
        MemberType memberType, SignUpType signUpType) {

        String rawRefreshToken = jwtProvider.createToken(email, memberId, memberType, signUpType,
            refreshExpireMillisecond);

        log.info("TokenProvider.createRefreshToken : Bearer {}", rawRefreshToken);

        RefreshToken refreshToken = RefreshToken.builder()
            .refreshToken(rawRefreshToken)
            .googleRefreshToken(googleRefreshToken)
            .memberId(memberId)
            .expiredAt(LocalDateTime.now().plusSeconds(refreshExpireMillisecond / 1000))
            .build();

        refreshTokenRepository.save(refreshToken);

        return String.format("%s%s", TOKEN_PREFIX, rawRefreshToken);
    }

    public boolean isNotBlankAndStartsWithBearer(String accessToken) {

        return StringUtils.hasText(accessToken) && accessToken.startsWith(TOKEN_PREFIX);
    }

    public boolean isAccessTokenInBlackList(Long memberId, String accessToken) {

        Optional<AccessTokenBlackList> accessTokenBlackList = accessTokenBlackListRepository.findById(
            memberId);

        if (accessTokenBlackList.isEmpty()) {
            return false;
        }

        return accessTokenBlackList.get().getBlackList()
            .contains(accessToken.substring(TOKEN_PREFIX.length()));
    }

    public void addAccessTokenToBlackList(Long memberId, String accessToken) {

        Optional<AccessTokenBlackList> optionalAccessTokenBlackList = accessTokenBlackListRepository.findById(
            memberId);

        AccessTokenBlackList accessTokenBlackList;

        if (optionalAccessTokenBlackList.isPresent()) {
            accessTokenBlackList = optionalAccessTokenBlackList.get();
            accessTokenBlackList.getBlackList().add(accessToken.substring(TOKEN_PREFIX.length()));
        } else {
            accessTokenBlackList = new AccessTokenBlackList(memberId, Arrays.asList(accessToken));
        }

        accessTokenBlackListRepository.save(accessTokenBlackList);

    }

    public void deleteRefreshTokenByMemberId(Long memberId) {
        refreshTokenRepository.deleteAll(refreshTokenRepository.findAllByMemberId(memberId));
    }


}
