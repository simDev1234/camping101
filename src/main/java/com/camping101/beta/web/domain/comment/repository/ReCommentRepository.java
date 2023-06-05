package com.camping101.beta.web.domain.comment.repository;

import com.camping101.beta.db.entity.comment.ReComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReCommentRepository extends JpaRepository<ReComment, Long> {

}
