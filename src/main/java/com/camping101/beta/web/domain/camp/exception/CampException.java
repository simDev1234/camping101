package com.camping101.beta.web.domain.camp.exception;

import lombok.Getter;

@Getter
public class CampException extends RuntimeException {

    private final ErrorCode errorCode;

    public CampException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public CampException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CampException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public CampException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public CampException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }
}
