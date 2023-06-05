package com.camping101.beta.web.domain.admin.recTag.service;

import com.camping101.beta.db.entity.regtag.RecTag;
import com.camping101.beta.web.domain.admin.recTag.dto.AdminRecTagCreateRequest;
import com.camping101.beta.web.domain.admin.recTag.exception.ErrorCode;
import com.camping101.beta.web.domain.admin.recTag.exception.RecTagException;
import com.camping101.beta.web.domain.regtag.dto.RecTagListRequest;
import com.camping101.beta.web.domain.regtag.dto.RecTagListResponse;
import com.camping101.beta.web.domain.regtag.repository.RecTagRepository;
import java.util.Objects;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminRecTagService {

    private final RecTagRepository recTagRepository;

    public void createRecTag(AdminRecTagCreateRequest request) {

        Optional<RecTag> optionalRecTag = recTagRepository.findByName(request.getName());

        if (optionalRecTag.isPresent()) {
            throw new RecTagException(ErrorCode.RECTAG_NOT_FOUND);
        }

        recTagRepository.save(RecTag.from(request));

    }

    public RecTagListResponse getAllRecTags(RecTagListRequest request) {

        Pageable page = PageRequest.of(request.getPageNumber(), request.getRecordSize(),
            Sort.Direction.DESC, "createdAt");

        Page<RecTag> rectags;
        if (Objects.isNull(request.getUseYn())) {
            rectags = recTagRepository.findAll(page);
        } else {
            rectags = recTagRepository.findByUseYnIs(request.getUseYn(), page);
        }
        return RecTagListResponse.fromEntity(rectags);
    }

    @Transactional
    public void activateOrDeactivateRecTag(String name, boolean useYn) {

        RecTag recTag = recTagRepository.findByName(name)
            .orElseThrow(() -> new RecTagException(ErrorCode.RECTAG_NOT_FOUND));

        recTag.setUseYn(useYn);

    }

    @Transactional
    public void deleteRecTag(Long recTagId) {

        RecTag recTag = recTagRepository.findById(recTagId)
            .orElseThrow(() -> new RecTagException(ErrorCode.RECTAG_NOT_FOUND));

        recTagRepository.delete(recTag);

    }

}
