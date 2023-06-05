package com.camping101.beta.web.domain.reservation.dto;

import com.camping101.beta.db.entity.site.Site;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindAllSitesByCampId {

    private Long siteId;
    private String siteName;

    public FindAllSitesByCampId(Site site) {
        this.siteId = site.getSiteId();
        this.siteName = site.getName();
    }
}
