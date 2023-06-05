package com.camping101.beta.db.entity.site;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SiteCapacity {

    private int basicCapacity;
    private int limitCapacity;
    private int parkingCapacity;

}
