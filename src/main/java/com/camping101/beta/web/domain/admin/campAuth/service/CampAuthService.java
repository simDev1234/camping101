package com.camping101.beta.web.domain.admin.campAuth.service;

import com.camping101.beta.db.entity.camp.Camp;
import com.camping101.beta.db.type.CampAuth;
import com.camping101.beta.web.domain.admin.campAuth.dto.PermitCampAuthRs;
import com.camping101.beta.web.domain.admin.campAuth.repository.CampAuthRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CampAuthService {

    private final FindCampAuthService findCampAuthService;
    private final CampAuthRepository campAuthRepository;

    // 캠핑장 상세에서 승인
    public PermitCampAuthRs permitCampAuth(Long campAuthId) {

        CampAuth findCampAuth = findCampAuthService.findCampAuthOrElseThrow(campAuthId);

        Camp findCamp = findCampAuth.getCamp();

        findCamp.editManageStatus();
        findCampAuth.editCampAuthStatus();

        return PermitCampAuthRs.createPermitAuthRs(findCampAuth, findCamp);
    }

    // 캠핑장 목록조회에서 단체 승인
    public List<PermitCampAuthRs> permitCampAuthList(List<Long> campAuthIds) {

        List<PermitCampAuthRs> rs = new ArrayList<>();

        for (Long campAuthId : campAuthIds) {

            PermitCampAuthRs permitCampAuthRs = permitCampAuth(campAuthId);
            rs.add(permitCampAuthRs);
        }

        return rs;

    }


}
