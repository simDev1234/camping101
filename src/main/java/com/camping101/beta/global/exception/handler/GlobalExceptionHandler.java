package com.camping101.beta.global.exception.handler;

import com.camping101.beta.web.domain.admin.recTag.exception.RecTagException;
import com.camping101.beta.web.domain.campLog.exception.CampLogException;
import com.camping101.beta.web.domain.comment.exception.CommentException;
import com.camping101.beta.web.domain.member.exception.MemberException;
import com.camping101.beta.web.domain.member.exception.TokenException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MemberException.class)
    public ResponseEntity<ExceptionResponse> handleMemberException(MemberException e) {

        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
            .status(e.getErrorCode().getHttpStatus().value())
            .reasonOfError(e.getErrorCode().getHttpStatus().getReasonPhrase())
            .errorMessage(e.getErrorCode().getMessage())
            .build();

        return new ResponseEntity<>(exceptionResponse, e.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(value = CommentException.class)
    public ResponseEntity<ExceptionResponse> handleCommentException(CommentException e) {

        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
            .status(e.getErrorCode().getHttpStatus().value())
            .reasonOfError(e.getErrorCode().getHttpStatus().getReasonPhrase())
            .errorMessage(e.getErrorCode().getMessage())
            .build();

        return new ResponseEntity<>(exceptionResponse, e.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(value = RecTagException.class)
    public ResponseEntity<ExceptionResponse> handleRecTagException(RecTagException e) {

        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
            .status(e.getErrorCode().getHttpStatus().value())
            .reasonOfError(e.getErrorCode().getHttpStatus().getReasonPhrase())
            .errorMessage(e.getErrorCode().getMessage())
            .build();

        return new ResponseEntity<>(exceptionResponse, e.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(value = CampLogException.class)
    public ResponseEntity<ExceptionResponse> handleCampLogException(CampLogException e) {

        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
            .status(e.getErrorCode().getHttpStatus().value())
            .reasonOfError(e.getErrorCode().getHttpStatus().getReasonPhrase())
            .errorMessage(e.getErrorCode().getMessage())
            .build();

        return new ResponseEntity<>(exceptionResponse, e.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(value = TokenException.class)
    public ResponseEntity<ExceptionResponse> handleTokenException(TokenException e) {

        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
            .status(e.getErrorCode().getHttpStatus().value())
            .reasonOfError(e.getErrorCode().getHttpStatus().getReasonPhrase())
            .errorMessage(e.getErrorCode().getMessage())
            .build();

        return new ResponseEntity<>(exceptionResponse, e.getErrorCode().getHttpStatus());
    }


    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> handleDataIntegrityViolationException(
        DataIntegrityViolationException e) {

        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
            .status(500)
            .reasonOfError("트랜잭션 처리 중 이슈가 발생했습니다.")
            .errorMessage(e.getMessage())
            .build();

        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleNullPointerException(NullPointerException e) {

        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
            .status(400)
            .reasonOfError("Null 값은 처리할 수 없습니다.")
            .errorMessage(e.getMessage())
            .build();

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> Exception(RuntimeException e) {

        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
            .status(500)
            .reasonOfError("?")
            .errorMessage(e.getMessage())
            .build();

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
