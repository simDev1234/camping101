package com.camping101.beta.web.domain.site.dto;

import com.camping101.beta.db.entity.site.Site;
import com.camping101.beta.db.entity.site.SiteCapacity;
import com.camping101.beta.db.entity.site.SiteType;
import com.camping101.beta.db.entity.site.SiteYn;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ModifySiteRs {

    private Long siteId;
    private Long campId;
    private String name;
    private String rpImage; //대표 이미지
    private String introduction;
    private SiteType type;
    private boolean openYn;
    private SiteYn siteYn;


    private LocalDateTime checkIn; // 체크 인 시간
    private LocalDateTime checkOut;// 체크 아웃 시간
    private int leastScheduling; // 최소 일정


    private SiteCapacity siteCapacity;
    private String mapImage;
    private String policy;
    private int price;
    private LocalDateTime refundableDate;

    public static ModifySiteRs createModifySiteRs(Site site) {

        return ModifySiteRs.builder()
            .siteId(site.getSiteId())
            .campId(site.getCamp().getCampId())
            .name(site.getName())
            .rpImage(site.getRpImage())
            .introduction(site.getIntroduction())
            .type(site.getType())
            .openYn(site.isOpenYn())
            .siteYn(site.getSiteYn())
            .checkIn(site.getCheckIn())
            .checkOut(site.getCheckOut())
            .leastScheduling(site.getLeastScheduling())
            .siteCapacity(site.getSiteCapacity())
            .mapImage(site.getMapImage())
            .policy(site.getPolicy())
            .price(site.getPrice())
            .refundableDate(site.getRefundableDate())
            .build();

    }

}
