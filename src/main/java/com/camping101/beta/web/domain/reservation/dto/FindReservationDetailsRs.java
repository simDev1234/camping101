package com.camping101.beta.web.domain.reservation.dto;

import com.camping101.beta.db.entity.reservation.Reservation;
import com.camping101.beta.db.entity.reservation.ReservationStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FindReservationDetailsRs {

    private Long reservationId;

    private Long memberId;
    private String nickname;

    private Long siteId;
    private String siteName;

    private LocalDate startDate;

    private LocalDate endDate;

    private int humanCapacity;

    private ReservationStatus status;

    private Long payment;

    private LocalDateTime createdAt;

    private LocalDateTime cancelAt;

    private boolean campLogYn; // 예약페이지에서 캠프로그를 작성할지 말지도 결정할 수 있기에 일단 캠프로그 썻는지와

    private boolean campLogWritableYn; // 캠프로그 작성권한이 있는지 넘겨준다.


    public static FindReservationDetailsRs createFindReservationDetailsRs(Reservation reservation) {

        return FindReservationDetailsRs.builder()
            .memberId(reservation.getMember().getMemberId())
            .nickname(reservation.getMember().getNickname())
            .reservationId(reservation.getReservationId())
            .siteId(reservation.getSite().getSiteId())
            .siteName(reservation.getSite().getName())
            .startDate(reservation.getStartDate())
            .endDate(reservation.getEndDate())
            .humanCapacity(reservation.getHumanCapacity())
            .status(reservation.getStatus())
            .payment(reservation.getPayment())
            .createdAt(reservation.getCreatedAt())
            .cancelAt(reservation.getCancelAt())
            .campLogYn(reservation.isCampLogYn())
            .campLogWritableYn(reservation.isCampLogWritableYn())
            .build();

    }
}
