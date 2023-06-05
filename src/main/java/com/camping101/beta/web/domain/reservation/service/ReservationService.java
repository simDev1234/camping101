package com.camping101.beta.web.domain.reservation.service;

import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.db.entity.reservation.Reservation;
import com.camping101.beta.db.entity.reservation.ReservationStatus;
import com.camping101.beta.db.entity.site.Site;
import com.camping101.beta.global.exception.CannotDeleteReservationException;
import com.camping101.beta.global.exception.CannotFindReservationException;
import com.camping101.beta.global.exception.DoubleBookingException;
import com.camping101.beta.web.domain.member.service.FindMemberService;
import com.camping101.beta.web.domain.reservation.dto.CreateReservationPaymentRq;
import com.camping101.beta.web.domain.reservation.dto.CreateReservationRq;
import com.camping101.beta.web.domain.reservation.dto.CreateReservationRs;
import com.camping101.beta.web.domain.reservation.dto.FindReservationListRs;
import com.camping101.beta.web.domain.reservation.repository.ReservationRepository;
import com.camping101.beta.web.domain.site.service.FindSiteService;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final FindMemberService findMemberService;
    private final FindSiteService findSiteService;
    private final FindReservationQueryService findReservationQueryService;
    private final FindReservationService findReservationService;

    // 사이트 예약 생성
//    1. 예약 가능 일자를 확인하고, 예약 일자를 선택한 후
//    2. 예약 버튼을 누르면 이용 정책을 안내
//    3. 확인 시 결제창으로 이동한다.
    public CreateReservationRs registerReservation(CreateReservationRq rq) {

        Site findSite = findSiteService.findSiteOrElseThrow(rq.getSiteId());

        Optional<Reservation> doubleBooking = reservationRepository.findBySiteAndStartDate(
            findSite, rq.getStartDate());

        if (doubleBooking.isPresent()) {
            throw new DoubleBookingException();
        }

        Reservation reservation = CreateReservationRq.createReservation(rq);

        reservationRepository.save(reservation);

        reservation.addPayment(rq.getPayment());

        Member findMember = findMemberService.findMemberOrElseThrow(rq.getMemberId());

        reservation.addMember(findMember);
        reservation.addSite(findSite);

        return CreateReservationRs.createReservationRs(reservation);
    }

    // 결제하기
    public Long payment(CreateReservationPaymentRq rq) {

        long days = ChronoUnit.DAYS.between(rq.getStartDate(), rq.getEndDate());
        return rq.getPrice() * days;
    }


    // 사이트 예약 목록 조회 (회원(손님)기능)
//    1. 회원(손님)은 최대 2년 이내의 예약 내역을 확인할 수 있다.
//    - 필터 : 3개월/6개월/1년/2년
//    - 없으면 “아직 예약을 하지 않았네요~ 예약을 해주세요”
//    2. 예약 사이트의 퇴실일이 지난 경우 캠프 로그 버튼이 활성화된다.
//    - [캠프 로그] 버튼 선택 시, 캠프 로그 등록 페이지로 이동
//    - 예약 목록 조회시 캠프 로그를 쓸수있는지 없는지가 나타남
    // -> 예약 목록 조회시 취소된 예약은 캠프로그를 작성할 수 없게 해야한다.
    public List<FindReservationListRs> findReservationFilterList(Long memberId,
        Optional<Integer> month, Pageable pageable) {

        List<Reservation> reservationList = findReservationQueryService.findReservationList(
            month,
            memberId, pageable);

        if (reservationList.size() == 0) {
            throw new CannotFindReservationException();
        }

        // 2. 아직 해당 예약에 대한 캠프로그를 작성하지 않았을 때 캠프로그 버튼 활성화
        // 조건 :  해당 예약이 예약한 기간을 지났으며, 캠프로그를 작성하지 않았고, 예약을 취소한적이 없는경우.
        modifyCampLogWritableYn(reservationList);

        return reservationList.stream().map(FindReservationListRs::createFindReservationListRs)
            .collect(Collectors.toList());
    }

    // 2. 캠프로그 버튼 활성화 => 예약 사이트의 퇴실일이 지난 경우 캠프 로그 버튼이 활성화된다.
    // 하나의 예약에 대해 여러개의 캠프로그는 쓸수없다. (단 하나의 캠프로그만 가능)
    private void modifyCampLogWritableYn(List<Reservation> reservationList) {

        for (Reservation reservation : reservationList) {

            // 해당 예약이 endDate를 지났을 경우
            if (reservation.getEndDate().isBefore(LocalDate.now())) {
                // 이미 캠프로그를 작성한적 있거나 예약이 취소된적 있으면 캠프로그를 더이상 작성할 수 없다.
                if (!reservation.isCampLogYn()
                    && reservation.getStatus() == ReservationStatus.COMP) {
                    reservation.changeCampLogWritableYn(reservation);
                }
            }
        }
    }


    // 사이트 예약 취소
//    1. 회원(손님)이 결제를 취소하면 회원(주인)의 카카오톡으로 자동 알림이 전송된다.
//    2. 예약 취소의 경우 일주일 전에만 가능하다.
    public void deleteReservation(Long reservationId) {

        Reservation findReservation = findReservationService.findByReservationOrElseThrow(
            reservationId);

        LocalDate localDate = findReservation.getStartDate().plusDays(7);
        LocalDate now = LocalDate.now();
        boolean before = localDate.isAfter(now);
        if (before) {
            Reservation.modifyReservationStatus(findReservation);
        } else {
            throw new CannotDeleteReservationException();
        }
    }
}
