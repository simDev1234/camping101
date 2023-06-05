package com.camping101.beta.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CannotFindCampException extends GeneralException {

    public CannotFindCampException() {
        super(HttpStatus.BAD_REQUEST, "캠핑장을 찾을 수 없습니다.");
    }

}
