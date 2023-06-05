package com.camping101.beta.web.domain.bookMark.dto;

import com.camping101.beta.db.entity.bookMark.BookMark;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateBookMarkRs {

    private Long bookMarkId;
    private Long memberId;
    private Long campLogId;
    private String title;

    public static CreateBookMarkRs createBookMarkRs(BookMark bookMark) {

        return CreateBookMarkRs.builder()
            .bookMarkId(bookMark.getBookMarkId())
            .memberId(bookMark.getMember().getMemberId())
            .campLogId(bookMark.getCampLog().getCampLogId())
            .title(bookMark.getCampLog().getTitle())
            .build();

    }

}
