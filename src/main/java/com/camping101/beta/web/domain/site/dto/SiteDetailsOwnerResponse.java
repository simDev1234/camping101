package com.camping101.beta.web.domain.site.dto;

import com.camping101.beta.db.entity.site.Site;
import com.camping101.beta.db.entity.site.SiteCapacity;
import com.camping101.beta.db.entity.site.SiteType;
import com.camping101.beta.db.entity.site.SiteYn;
import com.camping101.beta.web.domain.site.dto.sitedetailsresponse.CampLogDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class SiteDetailsOwnerResponse {

    private Long siteId;
    private Long campId;
    private String name;
    private String rpImage; // 대표이미지
    private String introduction;
    private SiteType type;
    private boolean openYn; // 이거를 활성화, 비활성화 , 탈퇴상태로 바꿀까?
    private SiteYn siteYn;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private int leastScheduling;
    private SiteCapacity siteCapacity;
    private String mapImage;
    private String policy;
    private int price;
    private LocalDateTime refundableDate;
    private List<CampLogDto> campLogDtoList;

    public SiteDetailsOwnerResponse(Site site) {
        this.siteId = site.getSiteId();
        this.campId = site.getCamp().getCampId();
        this.name = site.getName();
        this.rpImage = site.getRpImage();
        this.introduction = site.getIntroduction();
        this.type = site.getType();
        this.openYn = site.isOpenYn();
        this.siteYn = site.getSiteYn();
        this.checkIn = site.getCheckIn();
        this.checkOut = site.getCheckOut();
        this.leastScheduling = site.getLeastScheduling();
        this.siteCapacity = site.getSiteCapacity();
        this.mapImage = site.getMapImage();
        this.policy = site.getPolicy();
        this.price = site.getPrice();
        this.refundableDate = site.getRefundableDate();
        this.campLogDtoList = site.getCampLogList()
            .stream().map(CampLogDto::new).collect(Collectors.toList());
    }

}
