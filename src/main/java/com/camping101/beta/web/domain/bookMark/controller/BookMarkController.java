package com.camping101.beta.web.domain.bookMark.controller;

import com.camping101.beta.global.path.ApiPath;
import com.camping101.beta.web.domain.bookMark.dto.CreateBookMarkRq;
import com.camping101.beta.web.domain.bookMark.dto.CreateBookMarkRs;
import com.camping101.beta.web.domain.bookMark.dto.FindBookMarkListRs;
import com.camping101.beta.web.domain.bookMark.service.BookMarkService;
import com.camping101.beta.web.domain.bookMark.service.FindBookMarkService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "캠핑 101 - 북마크 API")
public class BookMarkController {

    private final BookMarkService bookMarkService;
    private final FindBookMarkService findBookMarkService;

    // 북마크 생성
    @PostMapping(ApiPath.BOOKMARK)
    public CreateBookMarkRs bookMarkAdd(@Validated @RequestBody
    CreateBookMarkRq rq) {

        return bookMarkService.registerBookMark(rq);

    }

    // 회원의 북마크 목록 조회
    // 북마크는 캠프로그를 북마크하는것이기에 캠프로그의 간단한 정보들도 포함되어 있어야 한다.
    @GetMapping(ApiPath.BOOKMARK_MEMBER_ID)
    public Page<FindBookMarkListRs> bookMarkList(
        @PathVariable("member-id") Long memberId,
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);

        return findBookMarkService.findBookMarkList(memberId, pageRequest);
    }

    // 북마크 삭제
    @DeleteMapping(ApiPath.BOOKMARK_ID)
    public void bookMarkDelete(@PathVariable("bookmark-id") Long bookMarkId) {

        bookMarkService.removeBookMark(bookMarkId);

    }


}
