package com.camping101.beta.web.domain.comment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    COMMENT_NOT_FOUND("댓글을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    COMMENT_MAX_LIMIT_REACHED("한 사람당 한 개의 댓글을 달 수 있으며, " +
        "한 캠프로그에는 최대 5개의 댓글이 달릴 수 있습니다.", HttpStatus.BAD_REQUEST),
    RE_COMMENT_MAX_LIMIT_REACHED("하나의 댓글에 하나의 대댓글만 작성 가능합니다.", HttpStatus.BAD_REQUEST),
    CAMPLOG_WRITER_MISMATCH("캠프로그 작성자만 대댓글을 작성할 수 있습니다.", HttpStatus.BAD_REQUEST),
    COMMENT_WRITER_MISMATCH("댓글을 작성한 사람만 수정 또는 삭제할 수 있습니다.", HttpStatus.BAD_REQUEST);


    private final String message;
    private final HttpStatus httpStatus;

}
