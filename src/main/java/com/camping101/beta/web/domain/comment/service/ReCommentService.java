package com.camping101.beta.web.domain.comment.service;

import com.camping101.beta.db.entity.comment.Comment;
import com.camping101.beta.db.entity.comment.ReComment;
import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.web.domain.comment.dto.ReCommentCreateRequest;
import com.camping101.beta.web.domain.comment.dto.ReCommentInfoResponse;
import com.camping101.beta.web.domain.comment.dto.ReCommentUpdateRequest;
import com.camping101.beta.web.domain.comment.exception.CommentException;
import com.camping101.beta.web.domain.comment.exception.ErrorCode;
import com.camping101.beta.web.domain.comment.repository.CommentRepository;
import com.camping101.beta.web.domain.comment.repository.ReCommentRepository;
import com.camping101.beta.web.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.camping101.beta.web.domain.comment.exception.ErrorCode.COMMENT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ReCommentService {

    private final CommentRepository commentRepository;
    private final ReCommentRepository reCommentRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ReCommentInfoResponse createReComment(ReCommentCreateRequest request) {

        Comment parentComment = commentRepository.findById(request.getParentId())
                .orElseThrow(() -> new CommentException(COMMENT_NOT_FOUND));

        Member reCommentWriter = memberRepository.findByEmail(request.getWriterEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        ReComment newReComment = reCommentRepository.save(ReComment.from(parentComment, reCommentWriter, request.getContent()));

        return ReCommentInfoResponse.fromEntity(newReComment);

    }

    @Transactional
    public ReCommentInfoResponse updateReComment(Long reCommentId, ReCommentUpdateRequest request) {

        ReComment reComment = reCommentRepository.findById(reCommentId)
                .orElseThrow(() -> new CommentException(COMMENT_NOT_FOUND));

        if (!reComment.getMember().getEmail().equals(request.getRequesterEmail())) {
            throw new CommentException(ErrorCode.COMMENT_WRITER_MISMATCH);
        }

        reComment.changeContent(request.getContent());

        return ReCommentInfoResponse.fromEntity(reComment);
    }

    @Transactional
    public void deleteReComment(Long reCommentId, String requesterEmail) {

        ReComment reComment = reCommentRepository.findById(reCommentId)
                .orElseThrow(() -> new CommentException(COMMENT_NOT_FOUND));

        if (!reComment.getMember().getEmail().equals(requesterEmail)) {
            throw new CommentException(ErrorCode.COMMENT_WRITER_MISMATCH);
        }

        reCommentRepository.delete(reComment);

    }

}
