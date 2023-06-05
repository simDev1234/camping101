package com.camping101.beta.web.domain.member.service.temporalPassword;

import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.db.entity.member.TemporalPassword;
import com.camping101.beta.util.CustomMailSender;
import com.camping101.beta.util.RandomCode;
import com.camping101.beta.web.domain.member.exception.ErrorCode;
import com.camping101.beta.web.domain.member.exception.MemberException;
import com.camping101.beta.web.domain.member.repository.MemberRepository;
import com.camping101.beta.web.domain.member.repository.TemporalPasswordRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemporalPasswordServiceImpl implements TemporalPasswordService {

    private final TemporalPasswordRepository temporalPasswordRepository;
    private final MemberRepository memberRepository;
    private final CustomMailSender customMailSender;

    @Value("${temporal.password.expiration.seconds}")
    private long temporalPasswordExpirationSeconds;

    @Override
    public void sendTemporalPassword(String email) {

        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));

        TemporalPassword tempPassword = createLimitedTimeTemporalPassword(member.getMemberId());

        sendTemporalPasswordToMail(member.getEmail(), tempPassword.getTemporalPassword());

        log.info("TemporalPasswordServiceImpl.sendTemporalPassword : 임시 비밀번호 발송 완료");

    }

    private TemporalPassword createLimitedTimeTemporalPassword(Long memberId) {

        temporalPasswordRepository.findByMemberId(memberId)
            .ifPresent(temporalPasswordRepository::delete);

        String randomTemporalPasswordCode = RandomCode.createRandomEightString();

        TemporalPassword newTemporalPassword = temporalPasswordRepository.save(
            TemporalPassword.builder()
                .temporalPassword(randomTemporalPasswordCode)
                .memberId(memberId)
                .build()
        );

        log.info(
            "TemporalPasswordServiceImpl.createLimitedTimeTemporalPassword : 임시 비밀번호 \"{}\" 생성",
            randomTemporalPasswordCode);

        return newTemporalPassword;
    }

    private void sendTemporalPasswordToMail(String email, String temporalPasswordCode) {

        String subject = "[Camping101] 캠핑 101에서 임시 비밀번호를 전송했습니다.";
        String htmlText = "<!DOCTYPE html>\n" +
            "<html lang=\"ko\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>Title</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "        <div>"
            + "임시 비밀번호는 [ " + temporalPasswordCode + "] 입니다." +
            "        </div>" +
            "</body>\n" +
            "</html>";

        boolean mailSendResult = customMailSender.sendMail(subject, "", htmlText, email);

        if (!mailSendResult) {
            throw new MemberException(ErrorCode.TEMPORAL_PASSWORD_ISSUE_FAIL);
        }
    }

    @Override
    public boolean isTemporalPasswordMatching(String temporalPassword, Long memberId) {

        Optional<TemporalPassword> optionalTemporalPassword = temporalPasswordRepository.findById(
            temporalPassword);

        if (optionalTemporalPassword.isEmpty()) {

            log.info(
                "TemporalPasswordServiceImpl.isTemporalPasswordMatching : 레디스에 저장된 임시 비밀번호 없음");

            return false;
        }

        return optionalTemporalPassword.get().getMemberId().equals(memberId);
    }

}
