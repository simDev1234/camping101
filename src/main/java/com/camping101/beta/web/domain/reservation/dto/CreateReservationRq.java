package com.camping101.beta.web.domain.reservation.dto;

import static com.camping101.beta.db.entity.reservation.ReservationStatus.COMP;

import com.camping101.beta.db.entity.reservation.Reservation;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class CreateReservationRq {

    private Long memberId;

    private Long siteId;

    private LocalDate startDate;

    private LocalDate endDate;

    private int humanCapacity;

    private Long payment;

    public static Reservation createReservation(CreateReservationRq createReservationRq) {

        return Reservation.builder()
            .startDate(createReservationRq.getStartDate())
            .endDate(createReservationRq.getEndDate())
            .humanCapacity(createReservationRq.getHumanCapacity())
            .status(COMP)
            .campLogYn(false)
            .campLogWritableYn(true)
            .build();

    }

}
