package com.camping101.beta.campLog.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CampLogListRequest {

    int pageNumber = 0;
    int recordSize = 10;

}
