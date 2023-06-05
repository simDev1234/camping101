package com.camping101.beta.web.domain.member.service.mypage;

import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.db.entity.member.status.MemberStatus;
import com.camping101.beta.db.entity.member.type.SignUpType;
import com.camping101.beta.util.S3FileUploader;
import com.camping101.beta.web.domain.member.dto.mypage.MemberInfoResponse;
import com.camping101.beta.web.domain.member.dto.mypage.MemberUpdateRequest;
import com.camping101.beta.web.domain.member.exception.ErrorCode;
import com.camping101.beta.web.domain.member.exception.MemberException;
import com.camping101.beta.web.domain.member.repository.MemberRepository;
import com.querydsl.core.util.StringUtils;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3FileUploader s3FileUploader;

    @Override
    public Long getMemberId(String signInMemberEmail) {

        Member member = memberRepository.findByEmail(signInMemberEmail)
            .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        return member.getMemberId();
    }

    @Override
    public void validateIfMemberIdMatchingWithPathMemberId(Long memberId, Long pathMemberId) {
        if (!Objects.equals(memberId, pathMemberId)) {
            throw new MemberException(ErrorCode.MEMBER_IS_NOT_MATCHING);
        }
    }

    @Override
    public MemberInfoResponse getMemberInfo(String signInMemberEmail, Long pathMemberId) {

        Member member = memberRepository.findById(pathMemberId)
            .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        validateIfMemberIdMatchingWithPathMemberId(member.getMemberId(), pathMemberId);

        return MemberInfoResponse.fromEntity(member);
    }

    @Override
    @Transactional
    public MemberInfoResponse updateMember(String email, Long memberId,
        MemberUpdateRequest request) {

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        validateIfMemberIdMatchingWithPathMemberId(member.getMemberId(), memberId);

        String newPassword = getNewPassword(member.getPassword(), request);
        String newProfileImagePath = getNewProfileImagePath(member.getProfileImagePath(), request);
        String newNickname = getNewNickname(member.getNickname(), request);
        String newPhoneNumber = getNewPhoneNumber(member.getPhoneNumber(), request);

        member.setPassword(newPassword);
        member.setProfileImagePath(newProfileImagePath);
        member.setNickname(newNickname);
        member.setPhoneNumber(newPhoneNumber);

        return MemberInfoResponse.fromEntity(member);
    }

    private String getNewPassword(String originalPassword, MemberUpdateRequest request) {
        return StringUtils.isNullOrEmpty(request.getPassword()) ?
            originalPassword : passwordEncoder.encode(request.getPassword());
    }

    private String getNewProfileImagePath(String originalProfileImage,
        MemberUpdateRequest request) {
        return Objects.isNull(request.getProfileImage()) || StringUtils.isNullOrEmpty(request.getProfileImage().getName()) ?
            originalProfileImage : s3FileUploader.uploadFileAndGetURL(request.getProfileImage());
    }

    private String getNewNickname(String originalNickname, MemberUpdateRequest request) {
        return StringUtils.isNullOrEmpty(originalNickname) ?
            originalNickname : request.getNickname();
    }

    private String getNewPhoneNumber(String originalPhoneNumber, MemberUpdateRequest request) {
        return StringUtils.isNullOrEmpty(originalPhoneNumber) ?
            originalPhoneNumber : request.getPhoneNumber();
    }

    @Override
    @Transactional
    public void deleteMember(String signInMemberEmail, Long pathMemberId) {

        Member member = memberRepository.findByEmail(signInMemberEmail)
            .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        validateIfMemberIdMatchingWithPathMemberId(member.getMemberId(), pathMemberId);

        log.info("MemberServeImpl.deleteMember : 다음 회원이 탈퇴를 요청했습니다 >> " + pathMemberId);

        member.setMemberStatus(MemberStatus.WITHDRAW);
        member.setDeletedAt(LocalDateTime.now());

    }

}
