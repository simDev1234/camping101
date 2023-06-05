package com.camping101.beta.web.domain.bookMark.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class CreateBookMarkRq {

    private Long memberId;
    private Long campLogId;
    private String title;


}
