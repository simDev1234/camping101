package com.camping101.beta.web.domain.regtag.dto;

import com.camping101.beta.db.entity.regtag.RecTag;
import java.util.ArrayList;
import java.util.List;
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
public class RecTagListResponse {

    long total;
    int pageNumber = 0;
    int recordSize = 10;
    List<RecTag> recTags = new ArrayList<>();

    public static RecTagListResponse fromEntity(Page<RecTag> response) {
        return RecTagListResponse.builder()
            .total(response.getTotalElements())
            .pageNumber(response.getNumber())
            .recordSize(response.getNumberOfElements())
            .recTags(response.getContent())
            .build();
    }
}
