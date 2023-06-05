package com.camping101.beta.web.domain.comment.dto;

import com.camping101.beta.global.paging.CustomPage;
import com.querydsl.core.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentListRequest extends CustomPage {

    private Long campLogId;

    CommentListRequest(Long campLogId, Integer pageNumber, Integer recordSize, String orderBy, String orderDir) {
        super(pageNumber == null || pageNumber < 1 ? 1 : pageNumber,
                recordSize == null || recordSize < 1 ? 10 : recordSize,
                StringUtils.isNullOrEmpty(orderBy) ? "createdAt" : orderBy,
                StringUtils.isNullOrEmpty(orderDir) ? "DESC" : orderDir
        );
        this.campLogId = campLogId;
    }

}
