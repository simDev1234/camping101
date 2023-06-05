package com.camping101.beta.web.domain.comment.repository;

import com.camping101.beta.db.entity.comment.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentCustomRepository {

    Page<Comment> findCommentListByCampLogId(Long campLogId, Pageable pageable);

}
