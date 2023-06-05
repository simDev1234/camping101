package com.camping101.beta.web.domain.site.dto.sitedetailsresponse;

import com.camping101.beta.db.entity.reservation.Reservation;
import com.camping101.beta.db.entity.reservation.ReservationStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ReservationDto {

    private Long reservationId;
    private Long memberId;
    private Long siteId;

    private LocalDate startDate;
    private LocalDate endDate;

    private int humanCapacity;
    private ReservationStatus status;

    private Long payment;
    private LocalDateTime createdAt;
    private LocalDateTime cancelAt;

    private boolean campLogYn;
    private boolean campLogWritableYn;


    public ReservationDto(Reservation reservation) {
        this.reservationId = reservation.getReservationId();
        this.memberId = reservation.getMember().getMemberId();
        this.siteId = reservation.getSite().getSiteId();
        this.startDate = reservation.getStartDate();
        this.endDate = reservation.getEndDate();
        this.humanCapacity = reservation.getHumanCapacity();
        this.status = reservation.getStatus();
        this.payment = reservation.getPayment();
        this.createdAt = reservation.getCreatedAt();
        this.cancelAt = reservation.getCancelAt();
        this.campLogYn = reservation.isCampLogYn();
        this.campLogWritableYn = reservation.isCampLogWritableYn();
    }
}
