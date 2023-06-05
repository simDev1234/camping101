package com.camping101.beta.web.domain.site.service;

import com.camping101.beta.db.entity.camp.Camp;
import com.camping101.beta.db.entity.reservation.Reservation;
import com.camping101.beta.db.entity.reservation.ReservationStatus;
import com.camping101.beta.db.entity.site.Site;
import com.camping101.beta.global.exception.CannotDeleteCampException;
import com.camping101.beta.web.domain.camp.service.FindCampService;
import com.camping101.beta.web.domain.site.dto.CreateSiteRq;
import com.camping101.beta.web.domain.site.dto.CreateSiteRs;
import com.camping101.beta.web.domain.site.dto.ModifySiteRq;
import com.camping101.beta.web.domain.site.dto.ModifySiteRs;
import com.camping101.beta.web.domain.site.repository.SiteRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SiteService {

    private final SiteRepository siteRepository;
    private final FindSiteService findSiteService;
    private final FindCampService findCampService;

    // 사이트 생성
    // 사이트가 실제로 보이려면 openYn을 사이트 수정에서 활성화시켜줘야 한다.
    public CreateSiteRs registerSite(CreateSiteRq rq) {

        Site site = CreateSiteRq.createSite(rq);

        siteRepository.save(site);

        Camp camp = findCampService.findCampOrElseThrow(rq.getCampId());

        site.addCamp(camp); // 변경감지로 넣기

        return CreateSiteRs.createSiteRs(site);

    }


    // 사이트 노출 상태 바꾸기 (주인기능) => 주인이 자신의 캠핑장의 사이트들을 목록 조회하면 그 이후
    // 체크박스를 통해 사이트들의 노출상태를 한번에 변경할 수 있음.
    // 만약 해당 사이트에 예약이 아직 존재한다면, 노출 상태를 변경할 수 없다.
    public void changeOpenYnTrue(List<Long> siteIds) {

        for (Long siteId : siteIds) {
            boolean isReservedSiteExist = isSiteReserved(siteId);

            if (!isReservedSiteExist) {
                Site findSite = findSiteService.findSiteOrElseThrow(siteId);
                findSite.changeOpenYn(findSite);
            }
        }
    }


    // 사이트 수정
    // 만약 해당 사이트에 예약이 아직 존재한다면, 노출 상태를 변경할 수 없다.
    public ModifySiteRs modifySite(ModifySiteRq rq) {

        Site findSite = findSiteService.findSiteOrElseThrow(rq.getSiteId());

        boolean isValid = isSiteReserved(findSite.getSiteId());

        if (isValid) {
            Site modifiedSite = findSite.updateSite(rq); // 변경 감지
            return ModifySiteRs.createModifySiteRs(modifiedSite);
        } else {
            log.info("{} 사이트에 예약이 존재해 영엽상태를 변경할 수 없습니다", findSite.getName());
            return ModifySiteRs.createModifySiteRs(findSite);
        }
    }

    // 하나의 사이트만 수정할때 사용
    public boolean isSiteReserved(Long siteId) {

        Site findSite = findSiteService.findSiteOrElseThrow(siteId);

        List<Reservation> reservationList = findSite.getReservationList();

        for (Reservation reservation : reservationList) {

            if (reservation.getStatus() == ReservationStatus.COMP &&
                !reservation.getEndDate().isAfter(LocalDate.now())) {
                // 이부분 어떻게 처리할지 고민하기.(우선 log 를 찍어서 임시방편으로 남겨둠)
                log.info("{} 사이트에 예약이 존재합니다.", findSite.getName());
                return true;
            }
        }
        return false;
    }

    // 사이트 삭제
    public void removeSite(Long siteId) {

        boolean isValid = isSiteReserved(siteId);

        if (!isValid) {
            siteRepository.deleteById(siteId);
        } else {
            throw new CannotDeleteCampException();
        }

    }


}
