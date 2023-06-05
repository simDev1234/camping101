package com.camping101.beta.web.domain.reservation.dto;

import com.camping101.beta.db.entity.reservation.Reservation;
import com.camping101.beta.db.entity.reservation.ReservationStatus;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindReservationListRs {

    private Long memberId;
    private Long reservationId;
    private Long siteId;
    private String siteName;
    private LocalDate startDate;
    private LocalDate endDate;
    private int humanCapacity;
    private ReservationStatus status;
    private Long payment;
    private LocalDateTime createdAt;
    private LocalDateTime cancelAt;
    private boolean campLogYn;
    private boolean campLogWritableYn;

    @QueryProjection
    public FindReservationListRs(Long memberId, Long reservationId, Long siteId, String siteName,
        LocalDate startDate, LocalDate endDate, int humanCapacity, ReservationStatus status,
        Long payment, LocalDateTime createdAt, LocalDateTime cancelAt, boolean campLogYn, boolean campLogWritableYn) {
        this.memberId = memberId;
        this.reservationId = reservationId;
        this.siteId = siteId;
        this.siteName = siteName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.humanCapacity = humanCapacity;
        this.status = status;
        this.payment = payment;
        this.createdAt = createdAt;
        this.cancelAt = cancelAt;
        this.campLogYn = campLogYn;
        this.campLogWritableYn = campLogWritableYn;
    }

    public static FindReservationListRs createFindReservationListRs(Reservation reservation) {

        return FindReservationListRs.builder()
            .memberId(reservation.getMember().getMemberId())
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
            .build();

    }
}
