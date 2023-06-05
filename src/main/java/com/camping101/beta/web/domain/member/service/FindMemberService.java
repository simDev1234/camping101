package com.camping101.beta.web.domain.member.service;

import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.global.exception.CannotFindMemberException;
import com.camping101.beta.web.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindMemberService {

    private final MemberRepository memberRepository;

    public Member findMemberOrElseThrow(Long memberId) {

        return memberRepository.findById(memberId).orElseThrow(CannotFindMemberException::new);
    }

}
