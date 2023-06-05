package com.camping101.beta.web.domain.reservation.service;

import com.camping101.beta.db.entity.camp.Camp;
import com.camping101.beta.db.entity.reservation.Reservation;
import com.camping101.beta.db.entity.site.Site;
import com.camping101.beta.global.exception.CannotFindReservationException;
import com.camping101.beta.web.domain.camp.service.FindCampService;
import com.camping101.beta.web.domain.reservation.dto.FindAllSitesByCampId;
import com.camping101.beta.web.domain.reservation.dto.FindReservationBySiteIdRs;
import com.camping101.beta.web.domain.reservation.dto.FindReservationDetailsRs;
import com.camping101.beta.web.domain.reservation.repository.ReservationRepository;
import com.camping101.beta.web.domain.site.service.FindSiteService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindReservationService {

    private final ReservationRepository reservationRepository;
    private final FindCampService findCampService;
    private final FindSiteService findSiteService;

    public Reservation findByReservationOrElseThrow(Long reservationId) {

        return reservationRepository.findById(reservationId).orElseThrow(
            CannotFindReservationException::new);

    }

//    // 캠핑장 예약 내역 목록 조회 (주인(캠핑장 사장) 기능) -> 초기 화면
//    // 캠핑장에 해당하는 모든 예약 내역 조회
//    public List<FindReservationBySiteIdRs> findSiteByCamp(Long campId) {
//
//        Camp findCamp = findCampService.findCampOrElseThrow(campId);
//
//        List<Long> siteIds = findCamp.getSites().stream()
//            .map(Site::getSiteId)
//            .collect(Collectors.toList());
//
//        List<FindReservationBySiteIdRs> rsList = new ArrayList<>();
//
//        for (Long siteId : siteIds) {
//
//            Site findSite = findSiteService.findSiteOrElseThrow(siteId);
//
//            FindReservationBySiteIdRs rs = new FindReservationBySiteIdRs(findSite);
//
//            for (Reservation reservation : findSite.getReservationList()) {
//
//                ReservedSiteInfo reservedSiteInfo = ReservedSiteInfo.createReservedSiteInfo(
//                    reservation);
//
//                rs.addReservedSiteInfo(reservedSiteInfo);
//            }
//
//            rsList.add(rs);
//        }
//
//        return rsList;
//    }

    public List<FindAllSitesByCampId> findAllSitesByCampId(Long campId) {
        Camp findCamp = findCampService.findCampOrElseThrow(campId);
        List<Site> sites = findCamp.getSites();
        return sites.stream().map(FindAllSitesByCampId::new).collect(Collectors.toList());

    }

    // 캠핑장 예약 목록 조회 (주인(캠핑장 사장) 기능) -> 검색 필터를 사용한 화면
    // 캠핑장에 해당 사이트의 모든 예약 목록 조회
    public List<FindReservationBySiteIdRs> findReservationBySiteId(Long siteId) {

        Site findSite = findSiteService.findSiteOrElseThrow(siteId);

        List<Reservation> reservationList = reservationRepository.findBySite(findSite);

        return reservationList.stream()
            .map(FindReservationBySiteIdRs::new)
            .collect(Collectors.toList());
    }

    // 사이트 예약 상세 조회 (회원(손님) + 주인(캠핑장 사장) 기능)
//   1. 회원(손님)은 마이페이제의 예약 목록 조회후 해당 예약 이름을 누르면 예약의 상세 정보를 볼 수 있다.
    public FindReservationDetailsRs findReservationDetails(Long reservationId) {

        Reservation findReservation = findByReservationOrElseThrow(reservationId);

        return FindReservationDetailsRs.createFindReservationDetailsRs(findReservation);
    }

}
