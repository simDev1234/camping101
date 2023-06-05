package com.camping101.beta.util;

import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.db.entity.member.type.MemberType;
import com.camping101.beta.db.entity.member.type.SignUpType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    @Value("${token.jwt.singing.key}")
    private String signingKey;

    public String createToken(Member member, long expiredSecond) {

        return createToken(member.getEmail(), member.getMemberId(),
            member.getMemberType(), member.getSignUpType(), expiredSecond);

    }

    public String createToken(String email, Long memberId, MemberType memberType,
        SignUpType signUpType, long expiredSecond) {

        Map<String, Object> payloads = new HashMap<>();
        payloads.put("email", email);
        payloads.put("memberId", memberId);
        payloads.put("memberType", memberType.name());
        payloads.put("signUpType", signUpType.name());

        long expiredTime = new Date().getTime() + expiredSecond;

        log.info("JwtProvider: Jwt 토큰 생성 >> 만료일은 " + new Date(expiredTime));

        return Jwts.builder()
            .setClaims(payloads)
            .setExpiration(new Date(expiredTime))
            .signWith(SignatureAlgorithm.HS256, signingKey.getBytes(StandardCharsets.UTF_8))
            .compact();
    }

    public Claims getClaim(String jwtToken) throws MalformedJwtException, ExpiredJwtException,
        SignatureException, UnsupportedJwtException {

        return Jwts.parser()
            .setSigningKey(signingKey.getBytes(StandardCharsets.UTF_8))
            .parseClaimsJws(jwtToken)
            .getBody();
    }

    public String getEmail(Claims claim) {
        return claim.get("email", String.class);
    }

    public Long getMemberId(Claims claim) {
        return claim.get("memberId", Long.class);
    }

    public MemberType getMemberType(Claims claim) {
        return MemberType.valueOf(claim.get("memberType", String.class));
    }

    public SignUpType getSignUpType(Claims claim) {
        return SignUpType.valueOf(claim.get("signUpType", String.class));
    }

}
