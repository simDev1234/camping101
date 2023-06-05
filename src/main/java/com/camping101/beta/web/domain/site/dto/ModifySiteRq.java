package com.camping101.beta.web.domain.site.dto;

import com.camping101.beta.db.entity.site.SiteCapacity;
import com.camping101.beta.db.entity.site.SiteType;
import com.camping101.beta.db.entity.site.SiteYn;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class ModifySiteRq {

    private Long siteId;

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

}
