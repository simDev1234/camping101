package com.camping101.beta.web.domain.member.repository;

import com.camping101.beta.db.entity.member.TemporalPassword;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemporalPasswordRepository extends CrudRepository<TemporalPassword, String> {

    Optional<TemporalPassword> findByMemberId(Long memberId);

}
