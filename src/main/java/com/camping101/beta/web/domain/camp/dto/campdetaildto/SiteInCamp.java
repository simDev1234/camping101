package com.camping101.beta.web.domain.camp.dto.campdetaildto;

import com.camping101.beta.db.entity.site.Site;
import com.camping101.beta.db.entity.site.SiteCapacity;
import com.camping101.beta.db.entity.site.SiteType;
import com.camping101.beta.web.domain.site.dto.sitedetailsresponse.ReservationDto;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SiteInCamp {

    private Long siteId;
    private String name;
    private String rpImage; //대표이미지
    private String introduction;
    private SiteType type; // 사이트 타입( 글램핑 등...)
    private boolean openYn; // 노출 상태, 비노출 상태
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private int leastScheduling;
    private SiteCapacity siteCapacity;
    private int price;
    private List<ReservationInSite> reservationInSiteList = new ArrayList<>();
    @QueryProjection
    public SiteInCamp(Site site) {
        this.siteId = site.getSiteId();
        this.name = site.getName();
        this.rpImage = site.getRpImage();
        this.introduction = site.getIntroduction();
        this.type = site.getType();
        this.openYn = site.isOpenYn();
        this.checkIn = site.getCheckIn();
        this.checkOut = site.getCheckOut();
        this.leastScheduling = site.getLeastScheduling();
        this.siteCapacity = site.getSiteCapacity();
        this.price = site.getPrice();
        this.reservationInSiteList = site.getReservationList().stream()
            .map(ReservationInSite::createReservationInSite).collect(Collectors.toList());
    }
}
