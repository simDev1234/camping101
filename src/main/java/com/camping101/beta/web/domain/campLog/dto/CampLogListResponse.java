package com.camping101.beta.web.domain.campLog.dto;

import com.camping101.beta.db.entity.campLog.CampLog;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CampLogListResponse {

    long total;
    int pageNumber = 0;
    int recordSize = 10;
    List<CampLogInfoResponse> campLogs = new ArrayList<>();

    public static CampLogListResponse fromEntity(Page<CampLog> campLogs) {
        return CampLogListResponse.builder()
            .total(campLogs.getTotalElements())
            .pageNumber(campLogs.getNumber())
            .recordSize(campLogs.getNumberOfElements())
            .campLogs(campLogs.getContent().stream()
                .map(CampLogInfoResponse::fromEntity)
                .collect(Collectors.toList()))
            .build();
    }
}
