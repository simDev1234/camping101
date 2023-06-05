package com.camping101.beta.web.domain.comment.exception;

import lombok.Getter;

@Getter
public class CommentException extends RuntimeException {

    private final ErrorCode errorCode;

    public CommentException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public CommentException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CommentException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public CommentException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public CommentException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }
}
