package com.camping101.beta.db.entity.site;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SiteStatus {

    RESERVED("예약상태"),
    AVAILABLE("이용가능상태");

    private final String name;

}
