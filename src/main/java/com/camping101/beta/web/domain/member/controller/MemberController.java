package com.camping101.beta.web.domain.member.controller;

import com.camping101.beta.global.security.authentication.MemberDetails;
import com.camping101.beta.web.domain.member.dto.mypage.MemberInfoResponse;
import com.camping101.beta.web.domain.member.dto.mypage.MemberSignOutRequest;
import com.camping101.beta.web.domain.member.dto.mypage.MemberUpdateRequest;
import com.camping101.beta.web.domain.member.service.mypage.MemberService;
import com.camping101.beta.web.domain.member.service.token.TokenService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Api(tags = "캠핑 101 - 마이페이지 API")
public class MemberController {

    private final MemberService memberService;
    private final TokenService tokenService;

    @GetMapping
    public ResponseEntity<MemberInfoResponse> myMemberInfo(
        @ApiIgnore @AuthenticationPrincipal MemberDetails memberDetails) {

        MemberInfoResponse memberInfoResponse = memberService.getMemberInfo(
            memberDetails.getEmail(), memberDetails.getMemberId());

        return ResponseEntity.ok(memberInfoResponse);
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MemberInfoResponse> memberUpdate(
            @ApiIgnore @AuthenticationPrincipal MemberDetails memberDetails,
            @ModelAttribute @RequestParam(name = "profileImage", required = false) MultipartFile image,
            MemberUpdateRequest request) {

        request.setProfileImage(image);

        MemberInfoResponse memberUpdateResponse
            = memberService.updateMember(memberDetails.getEmail(), memberDetails.getMemberId(),
            request);

        return ResponseEntity.ok(memberUpdateResponse);
    }

    @DeleteMapping("/signout")
    public ResponseEntity<Void> memberSignOut(
        @ApiIgnore @AuthenticationPrincipal MemberDetails memberDetails,
        @Valid @RequestBody MemberSignOutRequest request) {

        // 레디스의 Refresh Token 삭제
        tokenService.deleteRefreshTokenByMemberId(memberDetails.getMemberId());

        // 레디스에 Access Token을 Black List로 등록
        tokenService.addAccessTokenToBlackList(memberDetails.getMemberId(),
            request.getAccessToken());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> memberDelete(
        @ApiIgnore @AuthenticationPrincipal MemberDetails memberDetails) {

        memberService.deleteMember(memberDetails.getEmail(), memberDetails.getMemberId());

        return ResponseEntity.ok().build();
    }

}
