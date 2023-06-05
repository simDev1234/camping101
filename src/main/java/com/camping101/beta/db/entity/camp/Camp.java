package com.camping101.beta.db.entity.camp;

import static javax.persistence.EnumType.STRING;

import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.db.entity.site.Site;
import com.camping101.beta.db.type.CampAuth;
import com.camping101.beta.web.domain.camp.dto.ModifyCampRq;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
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
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Camp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "camp_id")
    private Long campId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private String name;

    private String intro;

    @Enumerated(STRING)
    private ManageStatus manageStatus;

    @Embedded
    private Location location;

    private String tel;
    private String oneLineReserveYn; // 데이터 확인하기 (불리언으로 데이터를 줄것같지 않다.)

    private String openSeason;
    private LocalDate openDateOfWeek;

    @Embedded
    private FacilityCnt facilityCnt;

    private String facility;
    private String leisure;
    private String animalCapable;
    private String equipmentTools;
    private String firstImage;
    private String homepage;
    private String businessNo;
    private Long campLogCnt;

    @CreatedDate
    @Column(updatable = false, insertable = true)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "camp", cascade = CascadeType.REMOVE)
    private List<Site> sites = new ArrayList<>();

    @OneToMany(mappedBy = "camp", cascade = CascadeType.REMOVE)
    private List<CampAuth> campAuthList = new ArrayList<>();

    public Camp updateCamp(ModifyCampRq modifyCampRq) {

        this.campId = modifyCampRq.getCampId();
        this.intro = modifyCampRq.getIntro();
        this.location = modifyCampRq.getLocation();
        this.tel = modifyCampRq.getTel();
        this.oneLineReserveYn = modifyCampRq.getOneLineReserveYn();
        this.openSeason = modifyCampRq.getOpenSeason();
        this.openDateOfWeek = modifyCampRq.getOpenDateOfWeek();
        this.facilityCnt = modifyCampRq.getFacilityCnt();
        this.facility = modifyCampRq.getFacility();
        this.leisure = modifyCampRq.getLeisure();
        this.animalCapable = modifyCampRq.getAnimalCapable();
        this.equipmentTools = modifyCampRq.getEquipmentTools();
        this.firstImage = modifyCampRq.getFirstImage();
        this.homepage = modifyCampRq.getHomepage();
        this.businessNo = modifyCampRq.getBusinessNo();

        return this;

    }

    public void addMember(Member member) {
        this.member = member;
    }

    public void editManageStatus() {
        this.manageStatus = ManageStatus.AUTHORIZED;

    }

    public void plusCampLogCnt() {
        this.campLogCnt++;
    }

    public void minusCampLogCnt() {
        this.campLogCnt--;
    }
}
