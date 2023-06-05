package com.camping101.beta.web.domain.campLog.exception;

import lombok.Getter;

@Getter
public class CampLogException extends RuntimeException {

    private final ErrorCode errorCode;

    public CampLogException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public CampLogException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CampLogException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public CampLogException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public CampLogException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }
}
