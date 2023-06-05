package com.camping101.beta.web.domain.member.controller;

import com.camping101.beta.web.domain.member.dto.signup.SignUpByEmailRequest;
import com.camping101.beta.web.domain.member.service.singup.MemberSignUpService;
import io.swagger.annotations.Api;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/signup")
@Api(tags = {"캠핑 101 - 회원가입 API"})
@Slf4j
public class MemberSignUpController {

    private final MemberSignUpService memberSignUpService;

    @PostMapping(value = "/mail", consumes = "multipart/form-data")
    public ResponseEntity<?> signUpByMail(@Valid SignUpByEmailRequest signUpByEmailRequest,
                                          @ModelAttribute @RequestParam(name = "profileImage", required = false) MultipartFile image) {

        signUpByEmailRequest.setProfileImage(image);

        memberSignUpService.signUpByEmail(signUpByEmailRequest);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/mail/auth")
    public ResponseEntity<?> activateMemberByAuthCode(@RequestParam String email,
        @RequestParam String mailAuthCode) {

        memberSignUpService.activateMemberByMailAuthCode(email, mailAuthCode);

        return ResponseEntity.ok().build();
    }

}
