package com.camping101.beta.web.domain.admin.member.service;

import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.db.entity.member.status.MemberStatus;
import com.camping101.beta.web.domain.admin.member.dto.AdminMemberInfoResponse;
import com.camping101.beta.web.domain.admin.member.dto.AdminMemberListRequest;
import com.camping101.beta.web.domain.admin.member.dto.AdminMemberListResponse;
import com.camping101.beta.web.domain.member.repository.MemberRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminMemberService {

    private final MemberRepository memberRepository;

    public AdminMemberListResponse getMemberList(AdminMemberListRequest request) {

        Pageable page = PageRequest.of(request.getPageNumber(), request.getRecordSize(),
            Sort.Direction.DESC, "createdAt");
        Page<Member> memberList = memberRepository.findMembersByMemberType(request.getMemberType(),
            page);

        return AdminMemberListResponse.fromEntity(memberList);
    }

    public AdminMemberInfoResponse getMemberInfo(Long memberId) {

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

        return AdminMemberInfoResponse.fromEntity(member);

    }

    @Transactional
    public AdminMemberInfoResponse updateMemberStatus(Long memberId, MemberStatus memberStatus) {

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

        member.setMemberStatus(memberStatus);

        return AdminMemberInfoResponse.fromEntity(member);

    }

}
