package com.camping101.beta.web.domain.member.repository;

import com.camping101.beta.db.entity.member.RefreshToken;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    List<RefreshToken> findAllByMemberId(Long memberId);

}
