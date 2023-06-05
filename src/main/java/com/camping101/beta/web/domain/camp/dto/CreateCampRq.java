package com.camping101.beta.web.domain.camp.dto;

import com.camping101.beta.db.entity.camp.Camp;
import com.camping101.beta.db.entity.camp.FacilityCnt;
import com.camping101.beta.db.entity.camp.Location;
import com.camping101.beta.db.entity.camp.ManageStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class CreateCampRq {

    private Long memberId;
    private String CampName;
    private String intro;
    private Location location;

    private String tel;
    private String oneLineReserveYn;

    private String openSeason;
    private LocalDate openDateOfWeek;
    private FacilityCnt facilityCnt;

    private String facility;
    private String leisure;
    private String animalCapable;
    private String equipmentTools;
    private String firstImage;
    private String homepage;
    private String businessNo; // 사업자 번호

    public static Camp createCamp(CreateCampRq rq) {

        return Camp.builder()
            .name(rq.getCampName())
            .intro(rq.getIntro())
            .manageStatus(ManageStatus.UNAUTHORIZED)
            .location(rq.getLocation())
            .tel(rq.getTel())
            .oneLineReserveYn(rq.getOneLineReserveYn())
            .openSeason(rq.getOpenSeason())
            .openDateOfWeek(rq.getOpenDateOfWeek())
            .facilityCnt(rq.getFacilityCnt())
            .facility(rq.getFacility())
            .leisure(rq.getLeisure())
            .animalCapable(rq.getAnimalCapable())
            .equipmentTools(rq.getEquipmentTools())
            .firstImage(rq.getFirstImage())
            .homepage(rq.getHomepage())
            .businessNo(rq.getBusinessNo())
            .build();

    }

}


