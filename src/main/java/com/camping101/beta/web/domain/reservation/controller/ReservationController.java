package com.camping101.beta.web.domain.reservation.controller;

import com.camping101.beta.global.path.ApiPath;
import com.camping101.beta.web.domain.reservation.dto.CreateReservationPaymentRq;
import com.camping101.beta.web.domain.reservation.dto.CreateReservationRq;
import com.camping101.beta.web.domain.reservation.dto.CreateReservationRs;
import com.camping101.beta.web.domain.reservation.dto.FindReservationBySiteIdRs;
import com.camping101.beta.web.domain.reservation.dto.FindReservationListRs;
import com.camping101.beta.web.domain.reservation.service.FindReservationService;
import com.camping101.beta.web.domain.reservation.service.ReservationService;
import io.swagger.annotations.Api;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "캠핑 101 - 예약 API")
public class ReservationController {

    private final ReservationService reservationService;
    private final FindReservationService findReservationService;

    // 사이트 예약 생성 => 에러가 너무 많이 터져서 보류
//    1. 예약 가능 일자를 확인하고, 예약 일자를 선택한 후
//    2. 예약 버튼을 누르면 이용 정책을 안내
//    3. 확인 시 결제창으로 이동한다.
    @PostMapping(ApiPath.RESERVATION)
    public CreateReservationRs reservationAdd(@RequestBody CreateReservationRq rq) {
        return reservationService.registerReservation(rq);

    }

    @PostMapping(ApiPath.RESERVATION_PAYMENT)
    public void paymentReservationPrice(@RequestBody CreateReservationPaymentRq rq) {
        reservationService.payment(rq);
    }

    // 사이트 예약 목록 조회(회원이 자신의 예약 내역 조회)
    // 해당 월의 예약 목록 가져오기
    // 필터 기능이 존재하는 회원의 예약 목록 조회
    @GetMapping(value = {ApiPath.RESERVATION_CUSTOMER_MEMBER_ID_MONTH, ApiPath.RESERVATION_CUSTOMER_MEMBER_ID})
    public List<FindReservationListRs> reservationFilterList(
        @PathVariable("member-id") Long memberId,
        @PathVariable(value = "month", required = false) Optional<Integer> month,
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return reservationService.findReservationFilterList(memberId, month, pageable);
    }

//    @GetMapping(ApiPath.RESERVATION_CAMP_CAMP_ID)
//    public List<FindAllSitesByCampId> siteList(@PathVariable("camp-id") Long campId) {
//
//        return findReservationService.findAllSitesByCampId(campId);
//    }

    // 캠핑장 사장의 자신의 캠핑장의 예약 목록 조회(혹은 자신의 캠핑장 사이트의 예약목록 조회)
    @GetMapping(ApiPath.RESERVATION_SITE_ID)
    public List<FindReservationBySiteIdRs> reservationList(@PathVariable("site-id") Long siteId) {

        return findReservationService.findReservationBySiteId(siteId);
    }

    // 사이트 예약 상세 조회
//    @GetMapping(ApiPath.RESERVATION_DETAILS_RESERVATION_ID)
//    public FindReservationDetailsRs reservationDetails(
//        @PathVariable("reservation-id") Long reservationId) {
//
//        return findReservationService.findReservationDetails(
//            reservationId);
//    }

    // 사이트 예약 취소
    @DeleteMapping(ApiPath.RESERVATION_ID)
    public void reservationRemove(@PathVariable("reservation-id") Long reservationId) {

        reservationService.deleteReservation(reservationId);

    }
}
