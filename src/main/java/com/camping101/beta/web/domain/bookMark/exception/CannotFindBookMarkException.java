package com.camping101.beta.web.domain.bookMark.exception;

import com.camping101.beta.global.exception.GeneralException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CannotFindBookMarkException extends GeneralException {


    public CannotFindBookMarkException() {
        super(HttpStatus.BAD_REQUEST, "북마크를 찾을 수 없습니다.");
    }
}
