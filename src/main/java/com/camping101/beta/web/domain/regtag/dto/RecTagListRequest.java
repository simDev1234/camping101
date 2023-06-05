package com.camping101.beta.web.domain.regtag.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecTagListRequest {

    int pageNumber = 0;
    int recordSize = 10;
    Boolean useYn;

}
