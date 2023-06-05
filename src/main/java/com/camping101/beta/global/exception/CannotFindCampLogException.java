package com.camping101.beta.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CannotFindCampLogException extends GeneralException {

    public CannotFindCampLogException() {
        super(HttpStatus.BAD_REQUEST, "캠프로그를 찾을 수 없습니다.");
    }

}
