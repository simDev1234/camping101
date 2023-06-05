package com.camping101.beta.web.domain.admin.recTag.dto;

import com.camping101.beta.db.entity.regtag.RecTag;
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
public class AdminRecTagInfoResponse {

    private Long recTagId;
    private String name;
    private boolean useYn;

    public static AdminRecTagInfoResponse fromEntity(RecTag recTag) {
        return AdminRecTagInfoResponse.builder()
            .recTagId(recTag.getRecTagId())
            .name(recTag.getName())
            .useYn(recTag.isUseYn())
            .build();
    }
}
