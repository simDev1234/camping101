package com.camping101.beta.web.domain.site.exception;

import lombok.Getter;

@Getter
public class SiteException extends RuntimeException {

    private final ErrorCode errorCode;

    public SiteException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public SiteException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public SiteException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public SiteException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public SiteException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }
}
