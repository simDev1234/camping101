package com.camping101.beta.web.domain.comment.dto;

import com.camping101.beta.db.entity.campLog.CampLog;
import com.camping101.beta.db.entity.comment.Comment;
import lombok.*;
import org.springframework.data.domain.Page;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentListResponse {

    long campLogId;
    long total;
    int pageNumber = 0;
    int recordSize = 5;
    List<CommentInfoResponse> comments = new ArrayList<>();

    public static CommentListResponse fromEntity(Long campLogId, Page<Comment> comments) {
        return CommentListResponse.builder()
                .campLogId(campLogId)
                .total(comments.getTotalElements())
                .pageNumber(comments.getNumber())
                .recordSize(comments.getNumberOfElements())
                .comments(comments.getContent().stream()
                        .map(CommentInfoResponse::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }

}
