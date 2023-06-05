package com.camping101.beta.web.domain.camp.service;

import com.camping101.beta.db.entity.camp.Camp;
import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.db.entity.site.Site;
import com.camping101.beta.db.type.CampAuth;
import com.camping101.beta.global.exception.CannotDeleteCampException;
import com.camping101.beta.web.domain.admin.campAuth.repository.CampAuthRepository;
import com.camping101.beta.web.domain.admin.campAuth.service.CampAuthService;
import com.camping101.beta.web.domain.camp.dto.CreateCampRq;
import com.camping101.beta.web.domain.camp.dto.CreateCampRs;
import com.camping101.beta.web.domain.camp.dto.ModifyCampRq;
import com.camping101.beta.web.domain.camp.dto.ModifyCampRs;
import com.camping101.beta.web.domain.camp.repository.CampRepository;
import com.camping101.beta.web.domain.member.service.FindMemberService;
import com.camping101.beta.web.domain.site.service.SiteService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CampService {

    private final CampRepository campRepository;
    private final FindMemberService findMemberService;
    private final SiteService siteService;
    private final FindCampService findCampService;
    private final CampAuthRepository campAuthRepository;

    // 캠핑장 서비스 이용 요청
    public CreateCampRs registerCamp(CreateCampRq rq) {

        Camp camp = CreateCampRq.createCamp(rq);
        campRepository.save(camp);

        Member findMember = findMemberService.findMemberOrElseThrow(rq.getMemberId());

        camp.addMember(findMember);

        requestCampAuth(camp);

        log.info("{} 캠핑장이 생성되었습니다. 관리자 승인이 완료되어야 캠핑장 서비스를 이용할 수 있습니다."
            , camp.getName());

        return CreateCampRs.createCampRs(camp);
    }

    // 관리자에게 캠핑장 생성 요청하기
    public void requestCampAuth(Camp camp) {

        CampAuth campAuth = CampAuth.createCampAuth(camp);

        campAuthRepository.save(campAuth);

    }

    // 사장의 마이페이지 -> 캠핑장 목록 조회 -> 캠핑장 상세 정보 보기 -> 캠핑장 상세 정보 수정 버튼 누름
    // -> 캠핑장 상세 정보 수정
    public ModifyCampRs modifyCamp(ModifyCampRq rq) {

        Camp camp = findCampService.findCampOrElseThrow(rq.getCampId());

        Camp modifiedCamp = camp.updateCamp(rq);

        return ModifyCampRs.createModifyCampRs(modifiedCamp);
    }

    // 캠핑장 서비스 탈퇴 요청
    // 캠핑장 탈퇴요청시 해당 캠핑장에 예약되어있는 사이트가 없어야 한다.
    public void removeCamp(Long campId) {

        Camp findCamp = findCampService.findCampOrElseThrow(campId);

        List<Long> siteIds = findCamp.getSites().stream().map(Site::getSiteId)
            .collect(Collectors.toList());

        boolean ableRemove = true;

        for (Long siteId : siteIds) {
            boolean isValid = siteService.isSiteReserved(siteId);
            if (isValid) {
                ableRemove = false;
            }
        }

        if (ableRemove) {
            campRepository.delete(findCamp);
        } else {
            throw new CannotDeleteCampException();
        }
    }

}
