package com.camping101.beta.web.domain.campLog.repository;

import com.camping101.beta.db.entity.campLog.CampLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampLogRepository extends JpaRepository<CampLog, Long> {

}
