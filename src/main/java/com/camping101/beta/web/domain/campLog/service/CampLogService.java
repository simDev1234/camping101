package com.camping101.beta.web.domain.campLog.service;

import com.camping101.beta.db.entity.camp.Camp;
import com.camping101.beta.db.entity.campLog.CampLog;
import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.db.entity.regtag.RecTag;
import com.camping101.beta.db.entity.site.Site;
import com.camping101.beta.util.RedisClient;
import com.camping101.beta.util.S3FileUploader;
import com.camping101.beta.web.domain.camp.service.FindCampService;
import com.camping101.beta.web.domain.campLog.dto.CampLogCreateRequest;
import com.camping101.beta.web.domain.campLog.dto.CampLogInfoResponse;
import com.camping101.beta.web.domain.campLog.dto.CampLogLikeKey;
import com.camping101.beta.web.domain.campLog.dto.CampLogLikeResponse;
import com.camping101.beta.web.domain.campLog.dto.CampLogListRequest;
import com.camping101.beta.web.domain.campLog.dto.CampLogListResponse;
import com.camping101.beta.web.domain.campLog.dto.CampLogUpdateRequest;
import com.camping101.beta.web.domain.campLog.exception.CampLogException;
import com.camping101.beta.web.domain.campLog.exception.ErrorCode;
import com.camping101.beta.web.domain.campLog.repository.CampLogRepository;
import com.camping101.beta.web.domain.member.repository.MemberRepository;
import com.camping101.beta.web.domain.regtag.repository.RecTagRepository;
import com.camping101.beta.web.domain.site.repository.SiteRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CampLogService {

    private final MemberRepository memberRepository;
    private final SiteRepository siteRepository;
    private final CampLogRepository campLogRepository;
    private final RecTagRepository recTagRepository;
    private final S3FileUploader s3FileUploader;
    private final RedisClient redisClient;
    private final Logger logger = Logger.getLogger(CampLogService.class.getName());

    @Transactional
    public CampLogInfoResponse createCampLog(CampLogCreateRequest request) {

        Member member = memberRepository.findByEmail(request.getWriterEmail())
            .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        Site site = siteRepository.findById(request.getSiteId())
            .orElseThrow(() -> new RuntimeException("Site Not Found")); // TODO 사이트 예외로 변경 필요

        CampLog newCampLog = campLogRepository.save(CampLog.from(request));

        Camp camp = site.getCamp();
        camp.plusCampLogCnt();

        try {
            List<String> imagePaths = saveImagesToS3AndGetPaths(request);
            newCampLog.changeImagePaths(imagePaths);
        } catch (IOException e) {
            Arrays.stream(e.getStackTrace()).forEach(x -> logger.info(x.toString()));
        }

        newCampLog.changeMember(member);
        newCampLog.changeRecTags(getRecTagsById("", request.getRecTags()));
        newCampLog.changeSite(site);

        return CampLogInfoResponse.fromEntity(newCampLog);

    }

    private List<String> saveImagesToS3AndGetPaths(CampLogCreateRequest request)
        throws IOException {

        List<String> imagePaths = new ArrayList<>();

        imagePaths.add(s3FileUploader.uploadFileAndGetURL(request.getImage()));
        imagePaths.add(s3FileUploader.uploadFileAndGetURL(request.getImage1()));
        imagePaths.add(s3FileUploader.uploadFileAndGetURL(request.getImage2()));
        imagePaths.add(s3FileUploader.uploadFileAndGetURL(request.getImage3()));
        imagePaths.add(s3FileUploader.uploadFileAndGetURL(request.getImage4()));
        imagePaths.add(s3FileUploader.uploadFileAndGetURL(request.getImage5()));

        return imagePaths;
    }

    private String getRecTagsById(String originalRecTags, List<Long> recTagIds) {

        StringBuilder sb = new StringBuilder();

        int idx = 0;
        for (Long recTagId : recTagIds) {
            Optional<RecTag> optionalRecTag = recTagRepository.findById(recTagId);
            if (optionalRecTag.isEmpty()) {
                logger.warning(
                    "CamLogService.getRecTagName : 요청한 추천 태그가 존재하지 않습니다. >> 추천 태그는 기존의 것을 유지");
                return originalRecTags;
            }
            idx++;
            if (idx > 4) {
                logger.info("추천 태그는 한 번에 4개까지만 등록이 가능합니다.");
                break;
            }
            if (idx != 0) {
                sb.append(",");
            }
            sb.append(optionalRecTag.get().getName());
        }

        return sb.toString();
    }

    public CampLogListResponse getCampLogList(CampLogListRequest request) {

        Pageable page = PageRequest.of(request.getPageNumber(), request.getRecordSize(),
            Sort.Direction.DESC, "createdAt");

        Page<CampLog> campLogs = campLogRepository.findAll(page);

        return CampLogListResponse.fromEntity(campLogs);
    }

    @Transactional
    public CampLogInfoResponse getCampLogInfo(Long campLogId) {

        CampLog campLog = campLogRepository.findById(campLogId)
            .orElseThrow(() -> new CampLogException(ErrorCode.CAMPLOG_NOT_FOUND));

        campLog.increaseViewCount();

        return CampLogInfoResponse.fromEntity(campLog);

    }

    @Transactional
    public CampLogInfoResponse updateCampLog(CampLogUpdateRequest request) {

        CampLog campLog = campLogRepository.findById(request.getCampLogId())
            .orElseThrow(() -> new CampLogException(ErrorCode.CAMPLOG_NOT_FOUND));

        if (!campLog.getMember().getEmail().equals(request.getRequesterEmail())) {

            logger.info("CampLogService.updateCampLog : 수정을 요청한 아이디가 작성자의 아이디와 일치하지 않습니다.");

            throw new CampLogException(ErrorCode.CAMPLOG_WRITER_MISMATCH);
        }

        campLog.updateCampLog(request);
        campLog.changeRecTags(getRecTagsById(campLog.getRecTags(), request.getRecTags()));

        try {
            List<String> imagePaths = saveImagesToS3AndGetPaths(request);
            campLog.changeImagePaths(imagePaths);
        } catch (IOException e) {
            logger.warning("CampLogService.updateCampLog : 이미지를 가져오던 중 이슈가 발생했습니다. (이미지 저장 실패)");
            Arrays.stream(e.getStackTrace()).forEach(x -> logger.warning(x.toString()));
        }

        logger.info("CampLogService.updateCampLog : 수정된 캠프 로그 ID -->  " + campLog.getCampLogId());

        return CampLogInfoResponse.fromEntity(campLog);
    }

    private List<String> saveImagesToS3AndGetPaths(CampLogUpdateRequest request)
        throws IOException {

        List<String> imagePaths = new ArrayList<>();

        imagePaths.add(s3FileUploader.uploadFileAndGetURL(request.getImage()));
        imagePaths.add(s3FileUploader.uploadFileAndGetURL(request.getImage1()));
        imagePaths.add(s3FileUploader.uploadFileAndGetURL(request.getImage2()));
        imagePaths.add(s3FileUploader.uploadFileAndGetURL(request.getImage3()));
        imagePaths.add(s3FileUploader.uploadFileAndGetURL(request.getImage4()));
        imagePaths.add(s3FileUploader.uploadFileAndGetURL(request.getImage5()));

        return imagePaths;
    }

    @Transactional
    public void deleteCampLog(Long campLogId, String requesterEmail) {

        CampLog campLog = campLogRepository.findById(campLogId)
            .orElseThrow(() -> new CampLogException(ErrorCode.CAMPLOG_NOT_FOUND));

        if (!campLog.getMember().getEmail().equals(requesterEmail)) {
            throw new CampLogException(ErrorCode.CAMPLOG_WRITER_MISMATCH);
        }

        campLogRepository.delete(campLog);

        Camp camp = campLog.getSite().getCamp();
        camp.minusCampLogCnt();

    }

    @Transactional
    public CampLogLikeResponse checkOrUncheckLikeOnCampLog(Long campLogId, String requesterEmail) {

        CampLog campLog = campLogRepository.findById(campLogId)
            .orElseThrow(() -> new CampLogException(ErrorCode.CAMPLOG_NOT_FOUND));

        String campLogLikeKey = CampLogLikeKey.getKey(campLogId, requesterEmail);
        Optional<Boolean> optionalLikeValue = redisClient.get(campLogLikeKey, Boolean.class);

        boolean isPreviouslyCheckedLike = false;
        if (!optionalLikeValue.isEmpty()) {
            isPreviouslyCheckedLike = optionalLikeValue.get();
            redisClient.delete(campLogLikeKey);
        }

        if (isPreviouslyCheckedLike == false) {
            campLog.increaseLikesCount();
        } else {
            campLog.decreaseLikesCount();
        }
        redisClient.put(campLogLikeKey, !isPreviouslyCheckedLike);

        return new CampLogLikeResponse(campLog.getCampLogId(), campLog.getLikes(),
            !isPreviouslyCheckedLike);
    }

}
