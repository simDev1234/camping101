package com.camping101.beta.web.domain.camp.dto.campdetaildto;

import com.camping101.beta.db.entity.reservation.Reservation;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ReservationInSite {

    private Long ReservationId;
    private LocalDate startDate;
    private LocalDate endDate;

    public static ReservationInSite createReservationInSite(Reservation reservation) {
        ReservationInSite reservationInSite = new ReservationInSite();
        reservationInSite.ReservationId = reservation.getReservationId();
        reservationInSite.startDate = reservation.getStartDate();
        reservationInSite.endDate = reservation.getEndDate();
        return reservationInSite;
    }
}
