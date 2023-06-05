package com.camping101.beta.web.domain.comment.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentUpdateRequest {

    @ApiModelProperty(hidden = true)
    private String requesterEmail;
    private Long campLogId;
    private String content;

}
