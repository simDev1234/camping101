package com.camping101.beta.web.domain.camp.dto;

import com.camping101.beta.db.entity.camp.FacilityCnt;
import com.camping101.beta.db.entity.camp.Location;
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
@Setter
@NoArgsConstructor
public class ModifyCampRq {

    private Long campId;
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
    private String businessNo;

}
