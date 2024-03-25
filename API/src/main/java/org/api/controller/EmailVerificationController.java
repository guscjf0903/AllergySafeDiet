package org.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.api.service.VerificationCodeService;
import org.core.request.EmailRequest;
import org.core.request.VerifyCodeRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class EmailVerificationController {
    private final VerificationCodeService verificationCodeService;

    @PostMapping("/email/verification_request")
    public ResponseEntity<Void> sendVerificationEmail(@Valid @RequestBody EmailRequest emailRequest) {
        verificationCodeService.sendCodeToEmail(emailRequest.email());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/emails/verifications")
    public ResponseEntity<Void> verificationEmail(@RequestBody VerifyCodeRequest verifyCodeRequest) {
        return verificationCodeService.validateEmailCodeFromRedis(verifyCodeRequest) ?
                ResponseEntity.ok().build() :
                ResponseEntity.badRequest().build();
    }
}
