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
    private final RedisService redisService;

    public void sendCodeToEmail(String email) {
        if(signupService.checkDuplicateMail(email)) {
            throw new CustomException(DUPLICATE_EMAIL);
        }
        String subject = "Email Verification";
        String code = createCode();
        redisService.saveVerificationCode(email, code); // 인증코드 redis에 저장
        mailService.sendMail(email, subject, code);
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

    public boolean verifyCode(String email, String submittedCode) {
        String storedCode = redisService.getVerificationCode(email);
        if (storedCode != null && storedCode.equals(submittedCode)) {
            redisService.deleteVerificationCode(email); // 코드 검증 후 삭제
            return true;
        }
        return false;
    }
}
