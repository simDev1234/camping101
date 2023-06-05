package com.camping101.beta.web.domain.member.service.signin.oAuth;

import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.util.JsonParser;
import com.camping101.beta.web.domain.member.dto.signin.oAuth.GoogleAccountInfo;
import com.camping101.beta.web.domain.member.dto.signin.oAuth.GoogleTokenInfo;
import com.camping101.beta.web.domain.member.dto.token.TokenInfo;
import com.camping101.beta.web.domain.member.exception.ErrorCode;
import com.camping101.beta.web.domain.member.exception.MemberException;
import com.camping101.beta.web.domain.member.repository.MemberRepository;
import com.camping101.beta.db.entity.member.RefreshToken;
import com.camping101.beta.web.domain.member.exception.TokenException;
import com.camping101.beta.web.domain.member.service.token.TokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.lang.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import javax.transaction.Transactional;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;
import static com.camping101.beta.global.config.GoogleOAuthConfig.*;
import static com.camping101.beta.web.domain.member.exception.ErrorCode.INVALID_REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleOAuthService implements OAuthService {

    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;
    private final TokenService tokenService;
    private final JsonParser jsonParser;

    @Override
    @Transactional(rollbackOn = {MemberException.class})
    public TokenInfo signInByOAuth(String code){

        GoogleTokenInfo googleTokenInfo = getGoogleTokenInfo(code);
        GoogleAccountInfo googleAccountInfo = parseIdTokenToGoogleAccountInfo(googleTokenInfo.getIdToken());

        Member member = createOrUpdateMember(googleAccountInfo);
        String accessToken = tokenService.createAccessToken(member);
        String refreshToken = tokenService.createRefreshToken(googleTokenInfo.getRefreshToken(), member);

        return new TokenInfo(accessToken, refreshToken);

    }

    // 구글 토큰 정보 조회 - 인증 코드를 통해 AccessToken, RefreshToken, idToken 발급
    private GoogleTokenInfo getGoogleTokenInfo(String code) throws MemberException{

        try {

            restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
            ResponseEntity<String> response = restTemplate.postForEntity(baseUriOf(googleTokenUri), tokenRequest(code), String.class);

            log.info("GoogleOAuthService.getGoogleTokenInfo : {}", response.getBody());

            GoogleTokenInfo googleTokenInfo = jsonParser.parseJsonToObject(response.getBody(), GoogleTokenInfo.class);

            return googleTokenInfo;

        } catch (JsonProcessingException e) {

            log.warn("GoogleOAuthService.exchangeAuthCodeForToken : Json parsing trouble");
            throw new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR);

        } catch (Exception e) {

            Arrays.stream(e.getStackTrace()).forEach(x -> log.warn(x.toString()));
            throw new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR);

        }

    }

    private HttpEntity<MultiValueMap<String, String>> tokenRequest(String code){
        HttpHeaders headers = requestHeadersOf(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = tokenRequestFormData(code);
        return new HttpEntity<>(body, headers);
    }

    private MultiValueMap<String, String> tokenRequestFormData(String code){
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", googleClientId);
        formData.add("client_secret", googleClientSecret);
        formData.add("redirect_uri", googleRedirectUri);
        formData.add("grant_type", "authorization_code");
        formData.add("code", code);
        return formData;
    }

    // 구글 회원 정보 가져오기 - idToken(JWT)의 payload를 파싱
    private GoogleAccountInfo parseIdTokenToGoogleAccountInfo(String idToken) {

        try {

            return jsonParser.parseJsonPayloadToObject(idToken, GoogleAccountInfo.class);

        } catch (JsonProcessingException e) {

            log.info("GoogleOAuthService.parseIdTokenGoogleAccountInfo : idToken의 payload를 json 파싱 중 오류 발생");
            throw new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR);

        } catch (Exception e) {

            log.info("GoogleOAuthService.parseIdTokenGoogleAccountInfo : idToken값이 잘못된 것 같습니다. 요청 정보를 다시 확인해주세요.");
            throw new MemberException(ErrorCode.MEMBER_SIGN_IN_ERROR);

        }
    }

    // 회원 생성 또는 수정
    private Member createOrUpdateMember(GoogleAccountInfo googleAccountInfo) {

        Optional<Member> optionalMember = memberRepository.findByEmail(googleAccountInfo.getEmail());

        if (optionalMember.isPresent()) {
            // 이미 가입한 경우 회원 프로필, 닉네임 업데이트
            Member member = optionalMember.get();
            member.setProfileImagePath(googleAccountInfo.getPicture());
            member.setNickname(googleAccountInfo.getName());
            return memberRepository.save(member);
        }

        return memberRepository.save(googleAccountInfo.toActivatedMember());
    }

    // 구글 엑세스 토큰 갱신
    @Override
    public String reissueAccessTokenByRefreshToken(String googleRefreshToken) throws TokenException{

        try {

            restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
            URI uri = baseUriOf(googleTokenUri);
            HttpEntity<MultiValueMap<String, String>> request = postRefreshTokenRequest(googleRefreshToken);
            ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);

            log.info("Google Response : \n" + response.getBody());

            GoogleTokenInfo googleTokenInfo = jsonParser.parseJsonToObject(response.getBody(), GoogleTokenInfo.class);

            return googleTokenInfo.getAccessToken();

        } catch (JsonProcessingException e) {

            log.warn("GoogleOAuthService.refreshGoogleToken : Json parsing trouble");
            throw new TokenException(INVALID_REFRESH_TOKEN);

        } catch (Exception e) {

            log.warn("GoogleOAuthService.refreshGoogleToken : Refresh Token을 전송하는 과정에서 무엇인가 잘못되었습니다!");
            Arrays.stream(e.getStackTrace()).forEach(x -> log.warn(x.toString()));
            throw new TokenException(INVALID_REFRESH_TOKEN);

        }
        
    }

    private HttpEntity<MultiValueMap<String, String>> postRefreshTokenRequest(String googleRefreshToken){
        HttpHeaders headers = requestHeadersOf(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = bodyOfRefreshTokenFormData(googleRefreshToken);
        return new HttpEntity<>(body, headers);
    }

    private MultiValueMap<String, String> bodyOfRefreshTokenFormData(String refreshToken){
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", googleClientId);
        formData.add("client_secret", googleClientSecret);
        formData.add("grant_type", "refresh_token");
        formData.add("refresh_token", refreshToken);
        return formData;
    }

    // 탈퇴 또는 로그아웃시 revoke 처리
    @Override
    public void revokeAccessTokenForLogOut(String serverAccessToken) {

        try {

            restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
            URI uri = baseUriOf(googleRevokeUri);
            String googleAccessToken = "tokenService.";
            HttpEntity<MultiValueMap<String, String>> request = postRevokeRequest(googleAccessToken);
            restTemplate.postForEntity(uri, request, String.class);

        } catch(MemberException e) {

            log.info("구글 로그아웃 중 이슈 : " + e.getMessage());

        } catch (Exception e) {

            log.warn("구글 로그아웃 중 이슈 : " + e.getMessage());

        }

    }

    private HttpEntity<MultiValueMap<String, String>> postRevokeRequest(String googleAccessToken){
        HttpHeaders headers = requestHeadersOf(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = bodyOfRevokeRequest(googleAccessToken);
        return new HttpEntity<>(body, headers);
    }

    private MultiValueMap<String, String> bodyOfRevokeRequest(String googleAccessToken){
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("token", googleAccessToken);
        return formData;
    }

    private URI baseUriOf(String USER_INFO_BASE_URI) {
        return UriComponentsBuilder
                .fromUriString(USER_INFO_BASE_URI)
                .encode()
                .build().toUri();
    }

    private HttpHeaders requestHeadersOf(MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        return headers;
    }



}
