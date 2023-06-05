package com.camping101.beta.web.domain.member.repository;

import com.camping101.beta.db.entity.member.MailAuth;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MailAuthRepository extends JpaRepository<MailAuth, String> {

    @Query("select m from MailAuth m join fetch m.member where m.member.email = :email")
    Optional<MailAuth> findByEmail(String email);

}
