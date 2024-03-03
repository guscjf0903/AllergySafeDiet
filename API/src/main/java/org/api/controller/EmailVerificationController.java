package org.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.api.service.VerificationCodeService;
import org.core.dto.EmailDto;
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
    public ResponseEntity<?> sendVerificationEmail(@Valid @RequestBody EmailDto emailDto) {
        verificationCodeService.sendCodeToEmail(emailDto.getEmail());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/emails/verifications")
    public ResponseEntity<?> verificationEmail(@RequestParam("email") @Valid String email,
                                            @RequestParam("verificationCode") String verificationCode) {
        boolean result = verificationCodeService.verifyCode(email, verificationCode);
        if(result) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
