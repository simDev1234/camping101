package com.camping101.beta.web.domain.campLog.controller;

import com.camping101.beta.web.domain.campLog.dto.CampLogCreateRequest;
import com.camping101.beta.web.domain.campLog.dto.CampLogInfoResponse;
import com.camping101.beta.web.domain.campLog.dto.CampLogLikeResponse;
import com.camping101.beta.web.domain.campLog.dto.CampLogListRequest;
import com.camping101.beta.web.domain.campLog.dto.CampLogListResponse;
import com.camping101.beta.web.domain.campLog.dto.CampLogUpdateRequest;
import com.camping101.beta.web.domain.campLog.service.CampLogService;
import io.swagger.annotations.Api;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/camplog")
@Api(tags = {"캠핑 101 - 캠프 로그 API"})
public class CampLogController {

    private final CampLogService campLogService;

    @PostMapping
    public ResponseEntity<CampLogInfoResponse> createCampLog(
        @RequestBody CampLogCreateRequest request,
        @ApiIgnore Principal principal) {

        request.setWriterEmail(principal.getName());
        CampLogInfoResponse createdCampLog = campLogService.createCampLog(request);

        return ResponseEntity.ok(createdCampLog);
    }

    @GetMapping
    public ResponseEntity<CampLogListResponse> getCampLogList(CampLogListRequest request) {

        CampLogListResponse campLogs = campLogService.getCampLogList(request);

        return ResponseEntity.ok(campLogs);
    }

    @GetMapping("/{campLogId}")
    public ResponseEntity<CampLogInfoResponse> getCampLogInfo(@PathVariable Long campLogId) {

        CampLogInfoResponse campLog = campLogService.getCampLogInfo(campLogId);

        return ResponseEntity.ok(campLog);
    }

    @PutMapping
    public ResponseEntity<CampLogInfoResponse> updateCampLog(
        @RequestBody CampLogUpdateRequest request,
        @ApiIgnore Principal principal) {

        request.setRequesterEmail(principal.getName());
        CampLogInfoResponse updatedCampLog = campLogService.updateCampLog(request);

        return ResponseEntity.ok(updatedCampLog);
    }

    @DeleteMapping("/{campLogId}")
    public ResponseEntity<?> deleteCampLog(@PathVariable Long campLogId,
        @ApiIgnore Principal principal) {

        campLogService.deleteCampLog(campLogId, principal.getName());

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{campLogId}")
    public ResponseEntity<CampLogLikeResponse> checkOrUncheckLikeOnCampLog(
        @PathVariable Long campLogId,
        @ApiIgnore Principal principal) {

        CampLogLikeResponse campLogLikeResponse
            = campLogService.checkOrUncheckLikeOnCampLog(campLogId, principal.getName());

        return new ResponseEntity(campLogLikeResponse, HttpStatus.OK);
    }

}
