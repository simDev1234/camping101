package com.camping101.beta.web.domain.comment.dto;

import com.camping101.beta.db.entity.comment.Comment;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentInfoResponse {

    private Long commentId;
    private String writerEmail;
    private String writerNickName;
    private String profileImagePath;
    private String content;
    private List<ReCommentInfoResponse> reComments;
    private LocalDateTime createdAt;

    public static CommentInfoResponse fromEntity(Comment comment){
        return CommentInfoResponse.builder()
                .commentId(comment.getCommentId())
                .writerEmail(comment.getMember().getEmail())
                .writerNickName(comment.getMember().getNickname())
                .profileImagePath(comment.getMember().getProfileImagePath())
                .content(comment.getContent())
                .reComments(comment.getReComments().stream()
                        .map(ReCommentInfoResponse::fromEntity)
                        .collect(Collectors.toList()))
                .createdAt(comment.getCreatedAt())
                .build();
    }

}
