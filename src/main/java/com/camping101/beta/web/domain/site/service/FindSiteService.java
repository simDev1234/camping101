package com.camping101.beta.web.domain.site.service;

import com.camping101.beta.db.entity.site.Site;
import com.camping101.beta.global.exception.CannotFindSiteException;
import com.camping101.beta.web.domain.camp.service.FindCampService;
import com.camping101.beta.web.domain.site.dto.sitedetailsresponse.FindSiteDetailsRs;
import com.camping101.beta.web.domain.site.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindSiteService {

    private final SiteRepository siteRepository;
    private final FindCampService findCampService;
    private final FindSiteQueryService findSiteQueryService;

    public Site findSiteOrElseThrow(Long siteId) {

        return siteRepository.findById(siteId).orElseThrow(CannotFindSiteException::new);

    }

    // 사이트 목록 조회(캠핑장 내의 사이트 조회) + 페이징 처리 하기 => 이거 회원은 필요없고, 주인만 있으면 된다.
    // 캠핑장 사장님 기능이다! => 마이페이지 -> 캠핑장 목록 조회 => (캠핑장목록에서 해당 캠핑장에 사이트 목록 조회 버튼
    // 만들기 => 해당 캠핑장의 사이트 목록 조회
//    public Page<SiteListResponse> findSiteList(Long campId) {
//
//        Camp camp = findCampService.findCampOrElseThrow(campId);
//
//        // 공개 여부가 true 인 값만 목록 조회
//        List<Site> sites = siteRepository.findAllByCampAndOpenYn(camp, true);
//
//        return sites.map(Site::toSiteListResponse);
//    }

    //     사이트 상세 조회
//     1. 주인(캠핑장 사장)은 사이트 상세 정보를 확인할 수 있다.
    public FindSiteDetailsRs findSiteDetails(Long siteId) {

        return findSiteQueryService.findSiteDetails(siteId);
    }

}
