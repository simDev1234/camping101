package com.camping101.beta.web.domain.site.dto.sitedetailsresponse;

import com.camping101.beta.db.entity.campLog.CampLog;
import com.camping101.beta.db.entity.regtag.RecTag;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CampLogDto {

    private Long campLogId;

    private Long memberId;

    private Long siteId;

    private String recTagDto;
    private String title;
    private LocalDateTime visitedAt;
    private String visitedWith;
    private String description;
    private String image;

    private long likes;
    private long view;


    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public CampLogDto(CampLog campLog) {
        this.campLogId = campLog.getCampLogId();
        this.memberId = campLog.getMember().getMemberId();
        this.siteId = campLog.getSite().getSiteId();
        this.recTagDto = campLog.getRecTags();
        this.title = campLog.getTitle();
        this.visitedAt = campLog.getVisitedAt();
        this.visitedWith = campLog.getVisitedWith();
        this.title = campLog.getTitle();
        this.description = campLog.getDescription();
        this.image = campLog.getImage();
        this.likes = campLog.getLikes();
        this.view = campLog.getView();
        this.createdAt = campLog.getCreatedAt();
        this.updatedAt = campLog.getUpdatedAt();
    }

    @Getter
    static class RecTagDto {

        private String reTagName;

        public RecTagDto(RecTag recTag) {
            reTagName = recTag.getName();
        }
    }
}
