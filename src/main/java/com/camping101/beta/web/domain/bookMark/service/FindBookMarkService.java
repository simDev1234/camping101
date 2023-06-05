package com.camping101.beta.web.domain.bookMark.service;

import com.camping101.beta.db.entity.bookMark.BookMark;
import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.global.exception.CannotFindBookMarkException;
import com.camping101.beta.web.domain.bookMark.dto.FindBookMarkListRs;
import com.camping101.beta.web.domain.bookMark.repository.BookMarkRepository;
import com.camping101.beta.web.domain.member.service.FindMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindBookMarkService {

    private final BookMarkRepository bookMarkRepository;
    private final FindMemberService findMemberService;

    public BookMark findBookMarkOrElseThrow(Long bookMarkId) {
        return bookMarkRepository.findById(bookMarkId)
            .orElseThrow(CannotFindBookMarkException::new);
    }

    // 북마크 목록 조회
    public Page<FindBookMarkListRs> findBookMarkList(Long memberId, Pageable pageable) {

        Member findMember = findMemberService.findMemberOrElseThrow(memberId);

        Page<BookMark> bookMarkList = bookMarkRepository.findBookMarkByMember(findMember, pageable);

        return bookMarkList.map(FindBookMarkListRs::createFindBookMarkListRs);

    }
}
