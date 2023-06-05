package com.camping101.beta.web.domain.reservation.dto;

import com.camping101.beta.db.entity.reservation.Reservation;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FindReservationBySiteIdRs {

    private String siteName;

//    private List<ReservedSiteInfo> reservedSiteInfoList = new ArrayList<>();

    private Long memberId;
    private String nickname;
    private Long reservationId;

    private LocalDate startDate;
    private LocalDate endDate;

    private int humanCapacity;
    private String reservationsStatus;
    private Long payment;
    private LocalDateTime createdAt; // 예약일
    private LocalDateTime cancelAt; // 취소일

//    public FindReservationBySiteIdRs(Site site) {
//
//        this.siteId = site.getSiteId();
//        this.siteName = site.getName();
//
//    }
//
//    public void addReservedSiteInfo(ReservedSiteInfo reservedSiteInfo) {
//        reservedSiteInfoList.add(reservedSiteInfo);
//
//    }


    public FindReservationBySiteIdRs(Reservation reservation) {
        this.siteName = reservation.getSite().getName();
        this.memberId = reservation.getMember().getMemberId();
        this.nickname = reservation.getMember().getNickname();
        this.reservationId = reservation.getReservationId();
        this.startDate = reservation.getStartDate();
        this.endDate = reservation.getEndDate();
        this.humanCapacity = reservation.getHumanCapacity();
        this.reservationsStatus = String.valueOf(reservation.getStatus());
        this.payment = reservation.getPayment();
        this.createdAt = reservation.getCreatedAt();
        this.cancelAt = reservation.getCancelAt();
    }
}
