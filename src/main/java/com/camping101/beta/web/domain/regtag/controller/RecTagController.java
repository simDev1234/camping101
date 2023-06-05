package com.camping101.beta.web.domain.regtag.controller;

import com.camping101.beta.web.domain.regtag.dto.RecTagListRequest;
import com.camping101.beta.web.domain.regtag.dto.RecTagListResponse;
import com.camping101.beta.web.domain.regtag.service.RecTagService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = {"캠핑 101 - 추천태그 조회 API"})
@RequestMapping("/api/camplog/regtag")
public class RecTagController {

    private final RecTagService recTagService;

    @GetMapping
    public ResponseEntity<RecTagListResponse> getAllRecTags(RecTagListRequest request) {

        RecTagListResponse recTags = recTagService.getAllRecTags(request);

        return ResponseEntity.ok(recTags);
    }

}
