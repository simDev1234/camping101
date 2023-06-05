package com.camping101.beta.web.domain.member.service.mypage;

import com.camping101.beta.web.domain.member.dto.mypage.MemberInfoResponse;
import com.camping101.beta.web.domain.member.dto.mypage.MemberUpdateRequest;

public interface MemberService {

    Long getMemberId(String signInMemberEmail);

    void validateIfMemberIdMatchingWithPathMemberId(Long memberId, Long pathMemberId);

    MemberInfoResponse getMemberInfo(String signInMemberEmail, Long pathMemberId);

    MemberInfoResponse updateMember(String email, Long memberId, MemberUpdateRequest request);

    void deleteMember(String signInMemberEmail, Long pathMemberId);

}
