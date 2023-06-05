package com.camping101.beta.web.domain.campLog.service;

import com.camping101.beta.db.entity.campLog.CampLog;
import com.camping101.beta.global.exception.CannotFindCampLogException;
import com.camping101.beta.web.domain.campLog.repository.CampLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindCampLogService {

    private final CampLogRepository campLogRepository;

    public CampLog findCampLogOrElseThrow(Long campLogId) {

        return campLogRepository.findById(campLogId).orElseThrow(CannotFindCampLogException::new);

    }

}
