package com.camping101.beta.web.domain.member.service.singup;

import com.camping101.beta.db.entity.member.MailAuth;
import com.camping101.beta.db.entity.member.Member;
import com.camping101.beta.util.RandomCode;
import com.camping101.beta.util.S3FileUploader;
import com.camping101.beta.web.domain.member.dto.signup.SignUpByEmailRequest;
import com.camping101.beta.web.domain.member.exception.ErrorCode;
import com.camping101.beta.web.domain.member.exception.MemberException;
import com.camping101.beta.web.domain.member.repository.MailAuthRepository;
import com.camping101.beta.web.domain.member.repository.MemberRepository;

import java.util.Objects;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberSignUpServiceImpl implements MemberSignUpService {

    private final MemberRepository memberRepository;
    private final MailAuthRepository mailAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailAuthService mailAuthService;
    private final S3FileUploader s3FileUploader;

    @Override
    @Transactional(rollbackOn = MemberException.class)
    public void signUpByEmail(SignUpByEmailRequest request) {

        validateIfMemberAlreadySignedUp(request.getEmail());

        Member newMember = createNewMember(request);
        sendMailAuthCodeToMemberEmail(newMember);

    }

    private void validateIfMemberAlreadySignedUp(String email) {

        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        if (optionalMember.isPresent()) {
            throw new MemberException(ErrorCode.MEMBER_SIGN_UP_ERROR);
        }

    }

    private Member createNewMember(SignUpByEmailRequest request) {

        String s3ProfileImagePath = "";

        if (Objects.nonNull(request.getProfileImage())) {
            s3ProfileImagePath = s3FileUploader.uploadFileAndGetURL(request.getProfileImage());
        }

        String encPassword = passwordEncoder.encode(request.getPassword());

        Member newMember = request.toNotActivatedMember(s3ProfileImagePath, encPassword);
        return memberRepository.save(newMember);

    }

    private void sendMailAuthCodeToMemberEmail(Member member) {

        String randomMailAuthCode = RandomCode.createRandomUUID();
        MailAuth mailAuth = mailAuthRepository.save(MailAuth.from(member, randomMailAuthCode));
        boolean mailSendResult = mailAuthService.sendMailAuthCode(member.getEmail(),
            mailAuth.getMailAuthCode());

        if (!mailSendResult) {
            throw new MemberException(ErrorCode.MAIL_AUTH_SEND_ERROR);
        }

    }

    public void signUpByMember(Member member) {
        memberRepository.save(member);
    }

    @Transactional
    public void activateMemberByMailAuthCode(String email, String mailAuthCode) {

        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new MemberException(ErrorCode.INVALID_MAIL_AUTH_REQUEST));

        Optional<MailAuth> optionalMailAuth = mailAuthRepository.findByEmail(email);

        validateMailAuthCode(mailAuthCode, optionalMailAuth);

        member.activateMember();

        mailAuthRepository.delete(optionalMailAuth.get());
    }

    private void validateMailAuthCode(String mailAuthCode, Optional<MailAuth> optionalMailAuth) {

        if (optionalMailAuth.isEmpty()) {

            log.info("이메일 인증 코드가 요청 Parameter에 없습니다.");

            throw new MemberException(ErrorCode.INVALID_MAIL_AUTH_REQUEST);
        }

        MailAuth mailAuth = optionalMailAuth.get();

        if (!mailAuthService.isAuthCodeMatching(mailAuth, mailAuthCode)) {

            log.info("이메일 인증 코드가 일치하지 않습니다. : 실제 코드 {}, 요청 코드 {}", mailAuth.getMailAuthCode(),
                mailAuthCode);

            throw new MemberException(ErrorCode.INVALID_MAIL_AUTH_REQUEST);
        }

        if (mailAuthService.isAuthCodeExpired(mailAuth)) {

            log.info("이메일 인증 코드 {}가 만료되어 재전송하였습니다");

            renewSignUpMailAuthCode(mailAuth);
            mailAuthService.sendMailAuthCode(mailAuth.getMember().getEmail(),
                mailAuth.getMailAuthCode());
            throw new MemberException(ErrorCode.EXPIRED_MAIL_AUTH);
        }

    }

    private void renewSignUpMailAuthCode(MailAuth mailAuth) {

        mailAuth.setMailAuthCode(RandomCode.createRandomUUID());
        mailAuthRepository.save(mailAuth);

    }

}
