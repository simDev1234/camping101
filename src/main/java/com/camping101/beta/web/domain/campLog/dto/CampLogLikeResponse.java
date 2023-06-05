package com.camping101.beta.web.domain.campLog.dto;

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
public class CampLogLikeResponse {

    private Long campLogId;
    private long totalLikes;
    private boolean myLikeCheck;

}
