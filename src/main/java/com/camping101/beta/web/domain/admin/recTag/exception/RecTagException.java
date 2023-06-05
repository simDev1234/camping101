package com.camping101.beta.web.domain.admin.recTag.exception;

import lombok.Getter;

@Getter
public class RecTagException extends RuntimeException {

    private final ErrorCode errorCode;

    public RecTagException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public RecTagException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public RecTagException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public RecTagException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public RecTagException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }
}
