//package com.camping101.beta.web.domain.reservation.dto;
//
//import com.camping101.beta.db.entity.reservation.Reservation;
//import java.time.LocalDateTime;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Getter
//@AllArgsConstructor
//@NoArgsConstructor
//public class ReservedSiteInfo {
//
//    private Long memberId;
//    private String nickname;
//    private Long reservationId;
//
//    private LocalDateTime startDate;
//    private LocalDateTime endDate;
//
//    private int humanCapacity;
//    private String reservationsStatus;
//    private Long payment;
//    private LocalDateTime createdAt; // 예약일
//    private LocalDateTime cancelAt; // 취소일
//
//    public static ReservedSiteInfo createReservedSiteInfo(Reservation reservation) {
//
//        ReservedSiteInfo reservedSiteInfo = new ReservedSiteInfo();
//        reservedSiteInfo.memberId = reservation.getMember().getMemberId();
//        reservedSiteInfo.nickname = reservation.getMember().getNickname();
//        reservedSiteInfo.reservationId = reservation.getReservationId();
//        reservedSiteInfo.startDate = reservation.getStartDate();
//        reservedSiteInfo.endDate = reservation.getEndDate();
//        reservedSiteInfo.humanCapacity = reservation.getHumanCapacity();
//        reservedSiteInfo.reservationsStatus = String.valueOf(reservation.getStatus());
//        reservedSiteInfo.payment = reservation.getPayment();
//        reservedSiteInfo.createdAt = reservation.getCreatedAt();
//        reservedSiteInfo.cancelAt = reservation.getCancelAt();
//
//        return reservedSiteInfo;
//    }
//}
