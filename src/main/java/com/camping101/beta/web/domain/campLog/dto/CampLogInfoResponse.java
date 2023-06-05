package com.camping101.beta.web.domain.campLog.dto;

import com.camping101.beta.db.entity.campLog.CampLog;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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
public class CampLogInfoResponse {

    private Long campLogId;
    private String writerEmail;
    private String writerNickName;
    private Long siteId;
    private LocalDateTime visitedAt;
    private String visitedWith;
    private List<String> recTags;
    private String title;
    private String description;
    private String image;
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;
    private long like;
    private long view;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CampLogInfoResponse fromEntity(CampLog campLog) {
        return CampLogInfoResponse.builder()
            .campLogId(campLog.getCampLogId())
            .writerEmail(campLog.getMember().getEmail())
            .writerNickName(campLog.getMember().getNickname())
            .siteId(campLog.getSite().getSiteId())
            .visitedAt(campLog.getVisitedAt())
            .visitedWith(campLog.getVisitedWith())
            .recTags(Arrays.stream(campLog.getRecTags().split(","))
                .collect(Collectors.toList()))
            .title(campLog.getTitle())
            .description(campLog.getDescription())
            .image(campLog.getImage())
            .image1(campLog.getImage1())
            .image2(campLog.getImage2())
            .image3(campLog.getImage3())
            .image4(campLog.getImage4())
            .image5(campLog.getImage5())
            .like(campLog.getLikes())
            .view(campLog.getView())
            .createdAt(campLog.getCreatedAt())
            .updatedAt(campLog.getUpdatedAt())
            .build();
    }

}
