package com.camping101.beta.web.domain.comment.service;

import com.camping101.beta.db.entity.campLog.CampLog;
import com.camping101.beta.db.entity.comment.Comment;
import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.web.domain.campLog.exception.CampLogException;
import com.camping101.beta.web.domain.campLog.repository.CampLogRepository;
import com.camping101.beta.web.domain.comment.dto.*;
import com.camping101.beta.web.domain.comment.exception.CommentException;
import com.camping101.beta.web.domain.comment.exception.ErrorCode;
import com.camping101.beta.web.domain.comment.repository.CommentRepository;
import com.camping101.beta.web.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.camping101.beta.web.domain.campLog.exception.ErrorCode.CAMPLOG_NOT_FOUND;
import static com.camping101.beta.web.domain.comment.exception.ErrorCode.COMMENT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final MemberRepository memberRepository;
    private final CampLogRepository campLogRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentInfoResponse createComment(CommentCreateRequest request) {

        CampLog campLog = campLogRepository.findById(request.getCampLogId())
                .orElseThrow(() -> new CampLogException(CAMPLOG_NOT_FOUND));

        Member commentWriter = memberRepository.findByEmail(request.getWriterEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        Comment newComment = commentRepository.save(Comment.from(campLog, commentWriter, request.getContent()));

        return CommentInfoResponse.fromEntity(newComment);

    }

    @Transactional(readOnly = true)
    public CommentListResponse getCommentListOfCampLog(CommentListRequest request) {

        Page<Comment> comments = commentRepository.findCommentListByCampLogId(request.getCampLogId(), request.toPageable());

        return CommentListResponse.fromEntity(request.getCampLogId(), comments);
    }

    @Transactional
    public CommentInfoResponse updateComment(Long commentId, CommentUpdateRequest request) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(COMMENT_NOT_FOUND));

        // 댓글 작성자만 수정이 가능하다.
        if (!comment.getMember().getEmail().equals(request.getRequesterEmail())) {
            throw new CommentException(ErrorCode.COMMENT_WRITER_MISMATCH);
        }

        comment.changeContent(request.getContent());

        return CommentInfoResponse.fromEntity(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, String requesterEmail) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(COMMENT_NOT_FOUND));

        // 댓글 작성자만 삭제 가능하다.
        if (!comment.getMember().getEmail().equals(requesterEmail)) {
            throw new CommentException(ErrorCode.COMMENT_WRITER_MISMATCH);
        }

        commentRepository.delete(comment);

    }

}
