package com.camping101.beta.web.domain.comment.repository;

import com.camping101.beta.db.entity.campLog.CampLog;
import com.camping101.beta.db.entity.comment.Comment;
import com.camping101.beta.db.entity.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentCustomRepository {

    Page<Comment> findAllByCampLog(CampLog campLog, Pageable pageable);
    //List<Comment> findAllByParentId(Long parentId);

}
