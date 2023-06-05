package com.camping101.beta.web.domain.admin.recTag.dto;

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
public class AdminRecTagCreateRequest {

    private Long campLogId;

    private String name;
    private boolean useYn;

}
