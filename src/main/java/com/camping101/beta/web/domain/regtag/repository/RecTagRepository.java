package com.camping101.beta.web.domain.regtag.repository;

import com.camping101.beta.db.entity.regtag.RecTag;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecTagRepository extends JpaRepository<RecTag, Long> {

    Optional<RecTag> findByName(String name);

    Page<RecTag> findByUseYnIs(boolean useYn, Pageable pageable);

}
