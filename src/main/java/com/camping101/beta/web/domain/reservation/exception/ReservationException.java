package com.camping101.beta.web.domain.reservation.exception;

import lombok.Getter;

@Getter
public class ReservationException extends RuntimeException {

    private final ErrorCode errorCode;

    public ReservationException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ReservationException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ReservationException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ReservationException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public ReservationException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }
}
