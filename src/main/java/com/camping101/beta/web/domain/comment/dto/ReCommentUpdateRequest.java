package com.camping101.beta.web.domain.comment.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReCommentUpdateRequest {

    @ApiModelProperty(hidden = true)
    private String requesterEmail;
    private Long campLogId;
    private String content;

}
