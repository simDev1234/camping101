package com.camping101.beta.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CannotFindSiteException extends GeneralException {

    public CannotFindSiteException() {
        super(HttpStatus.BAD_REQUEST, "사이트를 찾을 수 없습니다.");
    }


}
