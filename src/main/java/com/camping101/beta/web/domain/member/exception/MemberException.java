package com.camping101.beta.web.domain.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberException extends RuntimeException{

    private final ErrorCode errorCode;

}