package com.camping101.beta.web.domain.camp.service;

import static com.camping101.beta.db.entity.camp.ManageStatus.AUTHORIZED;

import com.camping101.beta.db.entity.camp.Camp;
import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.global.exception.CannotFindCampException;
import com.camping101.beta.web.domain.camp.dto.FindCampDetailsAdminRs;
import com.camping101.beta.web.domain.camp.dto.FindCampDetailsOwnerRs;
import com.camping101.beta.web.domain.camp.dto.FindCampListRs;
import com.camping101.beta.web.domain.camp.dto.campdetaildto.FindCampDetailsRs;
import com.camping101.beta.web.domain.camp.repository.CampRepository;
import com.camping101.beta.web.domain.member.service.FindMemberService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindCampService {

    private final CampRepository campRepository;
    private final FindMemberService findMemberService;
    private final FindCampQueryService findCampQueryService;

    public Camp findCampOrElseThrow(Long campId) {

        return campRepository.findById(campId).orElseThrow(CannotFindCampException::new);

    }

    // 주인의 캠핑장 목록 조회(페이징 처리 하기)
    public Page<FindCampListRs> findOwnerCampList(Pageable pageable, Long memberId) {

        Member findMember = findMemberService.findMemberOrElseThrow(memberId);

        Page<Camp> camps = campRepository.findCampByMember(pageable, findMember);

        return camps.map(FindCampListRs::createCampListRs);
    }

    // 캠핑장 목록 조회(페이징 처리 하기) (손님 + 비회원)
    public Page<FindCampListRs> findCampList(Pageable pageable) {

        Page<Camp> camps = campRepository.findAllByManageStatus(pageable, AUTHORIZED);
        List<Long> campLogCntList = camps.getContent().stream().map(Camp::getCampLogCnt)
            .collect(Collectors.toList());
        return camps.map(FindCampListRs::createCampListRs);
    }

    // 캠핑장 상세 정보 조회 -> 회원(손님)
    // 해당 캠핑장의 사이트 목록을 같이 가져온다.
    // 해당 사이트의 모든 캠프로그들도 같이 가져온다.
    public FindCampDetailsRs findCampDetails(Long campId, Pageable campLogPageable) {

        return findCampQueryService.findCampAndSiteAndCampLog(campId, campLogPageable);
    }

    public FindCampDetailsOwnerRs findCampDetailsOwner(Long campId) {

        Camp findCamp = findCampOrElseThrow(campId);

        return FindCampDetailsOwnerRs.createFindCampDetailsOwnerRs(findCamp);
    }

    // 캠핑장 상세 정보 조회 -> 주인(캠핑장 사장)
    // 관리자 페이지 -> 캠핑장 상세 정보 조회 (관리자)
    // 캠핑장 정보들만 제공한다.(사이트정보나 캠프로그 정보 제공 x)
    public FindCampDetailsAdminRs findCampDetailsAdmin(Long campId) {

        Camp findCamp = findCampOrElseThrow(campId);

        return FindCampDetailsAdminRs.createCampDetailsAdminRs(findCamp);
    }
}
