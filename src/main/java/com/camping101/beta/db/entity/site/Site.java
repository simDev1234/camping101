package com.camping101.beta.db.entity.site;

import com.camping101.beta.db.entity.camp.Camp;
import com.camping101.beta.db.entity.campLog.CampLog;
import com.camping101.beta.db.entity.reservation.Reservation;
import com.camping101.beta.web.domain.site.dto.ModifySiteRq;
import com.camping101.beta.web.domain.site.dto.SiteListResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "site_id")
    private Long siteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camp_id")
    private Camp camp;
    @OneToMany(mappedBy = "site")
    private List<Reservation> reservationList = new ArrayList<>();

    @OneToMany(mappedBy = "site")
    private List<CampLog> campLogList = new ArrayList<>();
    private String name;
    private String rpImage; //대표이미지
    private String introduction;

    @Enumerated(EnumType.STRING)
    private SiteType type;

    private boolean openYn; // 노출 상태, 비노출 상태

    @Embedded
    private SiteYn siteYn;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private int leastScheduling;

    @Embedded
    private SiteCapacity siteCapacity;

    private String mapImage;
    private String policy;
    private int price;
    private LocalDateTime refundableDate;

    public void addCamp(Camp camp) {

        this.camp = camp;
    }

    public void addReservation(List<Reservation> reservations) {
        this.reservationList = reservations;
    }

    public Site updateSite(ModifySiteRq modifySiteRq) {

        this.name = modifySiteRq.getName();
        this.rpImage = modifySiteRq.getRpImage(); //대표 이미지
        this.introduction = modifySiteRq.getIntroduction();
        this.type = modifySiteRq.getType();
        this.openYn = modifySiteRq.isOpenYn();
        this.siteYn = modifySiteRq.getSiteYn();
        this.checkIn = modifySiteRq.getCheckIn();
        this.checkOut = modifySiteRq.getCheckOut();
        this.leastScheduling = modifySiteRq.getLeastScheduling();
        this.siteCapacity = modifySiteRq.getSiteCapacity();
        this.mapImage = modifySiteRq.getMapImage();
        this.policy = modifySiteRq.getPolicy();
        this.price = modifySiteRq.getPrice();
        this.refundableDate = modifySiteRq.getRefundableDate();

        return this;
    }

    public static Site toEntity(ModifySiteRq modifySiteRq) {

        return Site.builder()
            .siteId(modifySiteRq.getSiteId())
            .name(modifySiteRq.getName())
            .rpImage(modifySiteRq.getRpImage())
            .introduction(modifySiteRq.getIntroduction())
            .type(modifySiteRq.getType())
            .siteYn(modifySiteRq.getSiteYn())
            .checkIn(modifySiteRq.getCheckIn())
            .checkOut(modifySiteRq.getCheckOut())
            .leastScheduling(modifySiteRq.getLeastScheduling())
            .siteCapacity(modifySiteRq.getSiteCapacity())
            .mapImage(modifySiteRq.getMapImage())
            .policy(modifySiteRq.getPolicy())
            .price(modifySiteRq.getPrice())
            .refundableDate(modifySiteRq.getRefundableDate())
            .build();

    }

    public static SiteListResponse toSiteListResponse(Site site) {

        return SiteListResponse.builder()
            .siteId(site.getSiteId())
            .campId(site.getCamp().getCampId())
            .name(site.getName())
            .rpImage(site.getRpImage())
            .checkIn(site.getCheckIn())
            .checkOut(site.getCheckOut())
            .leastScheduling(site.getLeastScheduling())
            .price(site.getPrice())
            .build();

    }

    public void changeOpenYn(Site site) {
        site.openYn = true;
    }
}
