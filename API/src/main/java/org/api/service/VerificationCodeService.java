package org.api.service;

import static org.api.exception.ErrorCodes.DUPLICATE_EMAIL;
import static org.api.exception.ErrorCodes.ERROR_CREATE_CODE;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.api.entity.UserEntity;
import org.api.entity.redis_entity.VerificationMailRedisEntity;
import org.api.exception.CustomException;
import org.api.repository.UserRepository;
import org.api.repository.redis_repository.VerificationMailRedisRepository;
import org.core.request.VerifyCodeRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class VerificationCodeService {
    private final MailService mailService;
    private final UserRepository userRepository;
    private final SignupService signupService;
    private final VerificationMailRedisRepository verificationMailRedisRepository;

    private static final String EMAIL_VERIFICATION_SUBJECT = "Email Verification";

    public void sendCodeToEmail(String email) {
        if (signupService.checkDuplicateMail(email)) {
            throw new CustomException(DUPLICATE_EMAIL);
        }
        String code = createCode();
        saveRedisVerificationCode(email, code); // 인증코드 redis에 저장
        mailService.sendMail(email, EMAIL_VERIFICATION_SUBJECT, code);
    }

    public void saveRedisVerificationCode(String email, String code) {
        VerificationMailRedisEntity verificationMailRedisEntity = new VerificationMailRedisEntity(email, code);
        verificationMailRedisRepository.save(verificationMailRedisEntity);
    }

    private String createCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (Exception e) {
            throw new CustomException(ERROR_CREATE_CODE);
        }
    }

    @Transactional
    public boolean validateEmailCodeFromRedis(VerifyCodeRequest verifyCodeRequest) {
        return verificationMailRedisRepository.findById(verifyCodeRequest.email())
                .filter(verificationMailRedisEntity -> verificationMailRedisEntity.getVerificationCode().equals(verifyCodeRequest.verificationCode()))
                .map(verificationMailRedisEntity -> {
                    userRepository.findByUserId(verifyCodeRequest.userPk())
                            .ifPresent(userEntity -> userEntity.emailUpdate(verifyCodeRequest.email()));
                    verificationMailRedisRepository.deleteById(verifyCodeRequest.email());
                    return true;
                })
                .orElse(false);
    }
}
