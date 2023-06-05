package com.camping101.beta.util;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomMailSender {

    private final JavaMailSender javaMailSender;

    public boolean sendMail(String subject, String plainText, String htmlText, String receiver) {

        boolean result = false;

        MimeMessagePreparator mimeMessagePreparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true,
                    "UTF-8");
                mimeMessageHelper.setSubject(subject);
                mimeMessageHelper.setText(plainText, htmlText);
                mimeMessageHelper.setTo(new InternetAddress(receiver));
            }
        };

        try {
            javaMailSender.send(mimeMessagePreparator);
            result = true;
        } catch (Exception e) {
            result = false;
        }

        return result;
    }

}
