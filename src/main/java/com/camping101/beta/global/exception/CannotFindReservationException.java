package com.camping101.beta.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CannotFindReservationException extends GeneralException {

    public CannotFindReservationException() {
        super(HttpStatus.BAD_REQUEST, "예약을 찾을 수 없습니다.");
    }


}
