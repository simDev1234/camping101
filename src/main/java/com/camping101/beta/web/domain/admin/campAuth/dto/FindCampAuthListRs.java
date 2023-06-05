package com.camping101.beta.web.domain.admin.campAuth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindCampAuthListRs {

    private Long campAuthId;
    private Long campId;
    private String campName;
    private String campAuthStatus;



//    private CampInfo campInfo;
//
//    static class CampInfo {
//
//        private Long id;
//        private String name;
//        ....
//    }

}
