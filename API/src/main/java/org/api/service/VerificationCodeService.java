package org.api.service;

import static org.api.exception.ErrorCodes.DUPLICATE_EMAIL;
import static org.api.exception.ErrorCodes.ERROR_CREATE_CODE;

import java.security.SecureRandom;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.api.exception.CustomException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VerificationCodeService {
    private final MailService mailService;
    private final SignupService signupService;

    public void sendCodeToEmail(String email) {
        if(signupService.checkDuplicateMail(email)) {
            throw new CustomException(DUPLICATE_EMAIL);
        }
        String subject = "Email Verification";
        String message = createCode();
        mailService.sendMail(email, subject, message);
    }

    private String createCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (Exception e) {
            throw new CustomException(ERROR_CREATE_CODE);
        }
    }
}
