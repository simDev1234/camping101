package com.camping101.beta.web.domain.admin.campAuth.dto;

import com.camping101.beta.db.entity.camp.Camp;
import com.camping101.beta.db.type.CampAuth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PermitCampAuthRs {

    private Long campAuthId;
    private Long campId;
    private String campName;
    private String campAuthStatus;

    public static PermitCampAuthRs createPermitAuthRs(CampAuth campAuth, Camp camp) {

        return PermitCampAuthRs.builder()
            .campAuthId(campAuth.getCampAuthId())
            .campId(camp.getCampId())
            .campName(camp.getName())
            .campAuthStatus(String.valueOf(campAuth.getCampAuthStatus()))
            .build();

    }

}
