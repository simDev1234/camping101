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
public class CreateReservationRs {

    private Long reservationId;

    private Long memberId;

    private Long siteId;

    private String siteName;

    private LocalDate startDate;

    private LocalDate endDate;

    private int humanCapacity;

    private ReservationStatus status;

    private Long payment;

    private LocalDateTime createdAt;

    private boolean campLogYn;

    private boolean campLogWritableYn;

    public static CreateReservationRs createReservationRs(Reservation reservation) {

        return CreateReservationRs.builder()
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
            .campLogYn(reservation.isCampLogYn())
            .campLogWritableYn(reservation.isCampLogWritableYn())
            .build();

    }


}
