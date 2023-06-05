package com.camping101.beta.web.domain.site.controller;

import com.camping101.beta.global.path.ApiPath;
import com.camping101.beta.web.domain.site.dto.CreateSiteRq;
import com.camping101.beta.web.domain.site.dto.CreateSiteRs;
import com.camping101.beta.web.domain.site.dto.ModifySiteRq;
import com.camping101.beta.web.domain.site.dto.ModifySiteRs;
import com.camping101.beta.web.domain.site.service.FindSiteService;
import com.camping101.beta.web.domain.site.service.SiteService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "캠핑 101 - 사이트 API")
public class SiteController {

    private final SiteService siteService;
    private final FindSiteService findSiteService;

    // 사이트 생성 => 최초 생성시 웹사이트에 공개하지 않음
    @PostMapping(ApiPath.SITE)
    public CreateSiteRs siteAdd(
        @RequestBody CreateSiteRq createSiteRq) {
        return siteService.registerSite(createSiteRq);
    }

    // 사이트 목록 조회
    // 대표이미지/ 사이트명 /가격/기본일정/체크인
    // 회원은 사이트 공개여부를 체크할 수 있다.
    // 단,현재일로 부터 예약된 날짜가 하나라도 있는 경우에 공개 여부를 Y/N으로 바꿀 수 없다.
//    @GetMapping(ApiPath.SITE_CAMP_ID)
//    public ResponseEntity<Page<SiteListResponse>> siteList(@PathVariable("camp-id") Long campId) {
//
////        PageRequest pageRequest = PageRequest.of(page, size);
//
//        Page<SiteListResponse> rs = findSiteService.findSiteList(campId);
//        return ResponseEntity.ok(rs);
//    }

//    // 사이트 상세 조회 (캠핑장 주인 기능)
//    @GetMapping(ApiPath.SITE_ID)
//    public ResponseEntity<FindSiteDetailsRs> siteDetailsOwner(@PathVariable("site-id") Long siteId) {
//
//        FindSiteDetailsRs rs = findSiteService.findSiteDetails(siteId);
//
//        return ResponseEntity.ok(rs);
//    }

    //////////////// 사이트 open상태 true로 한번에 바꾸는 컨트롤러 추가 필요////////////////////

    // 사이트 수정
    @PutMapping(ApiPath.SITE)
    public ModifySiteRs siteModify(
        @RequestBody ModifySiteRq modifySiteRq) {

        return siteService.modifySite(modifySiteRq);
    }

    // 사이트 삭제
    @DeleteMapping(ApiPath.SITE_ID)
    public void siteRemove(@PathVariable("site-id") Long siteId) {

        siteService.removeSite(siteId);
    }
}
