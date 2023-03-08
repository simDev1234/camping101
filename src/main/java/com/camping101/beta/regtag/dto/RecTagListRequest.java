package com.camping101.beta.regtag.dto;

import lombok.*;

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
