package com.camping101.beta.web.domain.comment.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReCommentCreateRequest {

    @ApiModelProperty(hidden = true)
    private String writerEmail;
    private Long campLogId;
    private long parentId;
    private String content;

}
