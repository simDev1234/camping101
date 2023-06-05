package com.camping101.beta.web.domain.admin.member.dto;

import com.camping101.beta.db.entity.member.type.MemberType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminMemberListRequest {

    int pageNumber = 0;
    int recordSize = 10;
    MemberType memberType = MemberType.CUSTOMER;

}
