package com.camping101.beta.web.domain.admin.member.dto;

import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.db.entity.member.type.MemberType;
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
public class AdminMemberListResponse {

    long total;
    int pageNumber = 0;
    int recordSize = 10;
    String memberType = MemberType.CUSTOMER.name();
    List<AdminMemberInfoResponse> content = new ArrayList<>();


    public static AdminMemberListResponse fromEntity(Page<Member> response) {
        return AdminMemberListResponse.builder()
            .total(response.getTotalElements())
            .pageNumber(response.getNumber())
            .recordSize(response.getNumberOfElements())
            .memberType(response.getContent().get(0).getMemberType().name())
            .content(response.getContent().stream()
                .map(AdminMemberInfoResponse::fromEntity).collect(Collectors.toList()))
            .build();
    }
}
