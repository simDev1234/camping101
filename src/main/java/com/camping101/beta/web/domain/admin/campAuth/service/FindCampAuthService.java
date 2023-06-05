package com.camping101.beta.web.domain.admin.campAuth.service;

import com.camping101.beta.db.type.CampAuth;
import com.camping101.beta.global.exception.CannotFindCampAuthException;
import com.camping101.beta.web.domain.admin.campAuth.dto.FindCampAuthListRs;
import com.camping101.beta.web.domain.admin.campAuth.repository.CampAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindCampAuthService {

    private final CampAuthRepository campAuthRepository;

    public CampAuth findCampAuthOrElseThrow(Long campAuthId) {

        return campAuthRepository.findById(campAuthId)
            .orElseThrow(CannotFindCampAuthException::new);
    }

    //      1. 회원(주인)은 회원가입 후에 캠핑장 관리 서비스 이용 요청을 할 수 있다.
//      2. 관리자가 승인을 하면 캠핑장 관리 서비스 이용 가능
    public Page<FindCampAuthListRs> findCampAuthList(Pageable pageable) {

        Page<CampAuth> campAuthList = campAuthRepository.findAll(pageable);

        return campAuthList.map(campAuth -> new FindCampAuthListRs(
            campAuth.getCampAuthId(),
            campAuth.getCamp().getCampId(),
            campAuth.getCamp().getName(),
            String.valueOf(campAuth.getCampAuthStatus())));
    }

}
