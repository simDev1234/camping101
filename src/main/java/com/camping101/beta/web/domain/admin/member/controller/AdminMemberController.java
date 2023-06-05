package com.camping101.beta.web.domain.admin.member.controller;

import com.camping101.beta.db.entity.member.status.MemberStatus;
import com.camping101.beta.web.domain.admin.member.dto.AdminMemberInfoResponse;
import com.camping101.beta.web.domain.admin.member.dto.AdminMemberListRequest;
import com.camping101.beta.web.domain.admin.member.dto.AdminMemberListResponse;
import com.camping101.beta.web.domain.admin.member.service.AdminMemberService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/member")
@Api(tags = "관리자사이트 - 회원 관리 API")
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    @GetMapping
    public ResponseEntity<AdminMemberListResponse> getMemberList(AdminMemberListRequest request) {

        AdminMemberListResponse memberList = adminMemberService.getMemberList(request);

        return ResponseEntity.ok(memberList);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<AdminMemberInfoResponse> getMemberInfo(@PathVariable Long memberId) {

        AdminMemberInfoResponse memberInfo = adminMemberService.getMemberInfo(memberId);

        return ResponseEntity.ok(memberInfo);
    }

    @PutMapping
    public ResponseEntity<AdminMemberInfoResponse> updateMemberStatus(@RequestParam Long memberId,
        @RequestParam MemberStatus memberStatus) {

        AdminMemberInfoResponse updatedMember = adminMemberService.updateMemberStatus(memberId,
            memberStatus);

        return ResponseEntity.ok(updatedMember);
    }

}
