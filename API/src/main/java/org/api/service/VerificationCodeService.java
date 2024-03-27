package org.api.service;

import static org.api.exception.ErrorCodes.DUPLICATE_EMAIL;
import static org.api.exception.ErrorCodes.ERROR_CREATE_CODE;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.api.exception.CustomException;
import org.api.repository.UserRepository;
import org.api.repository.redis_repository.VerificationMailRedisRepository;
import org.core.request.VerifyCodeRequest;
import org.springframework.cache.annotation.CachePut;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class VerificationCodeService {
    private final MailService mailService;
    private final UserRepository userRepository;
    private final SignupService signupService;
    private final StringRedisTemplate stringRedisTemplate;


    private static final String EMAIL_VERIFICATION_SUBJECT = "Email Verification";

    public void sendCodeToEmail(String email) {
        if (signupService.checkDuplicateMail(email)) {
            throw new CustomException(DUPLICATE_EMAIL);
        }
        String code = createCodeAndSaveRedis(email);
        System.out.println(code);
        mailService.sendMail(email, EMAIL_VERIFICATION_SUBJECT, code);
    }


    //@CachePut(value = "verificationCode", key = "#email") //스프링 캐시를 사용하여 이메일 인증코드 저장. (동일한 키값 생성시 덮어 씌움)
    public String createCodeAndSaveRedis(String email) {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            String code = builder.toString();
            stringRedisTemplate.opsForValue().set(email, code, Duration.ofMinutes(5));

            return code;
        } catch (Exception e) {
            throw new CustomException(ERROR_CREATE_CODE);
        }
    }

    @Transactional
    public boolean validateEmailCodeFromRedis(VerifyCodeRequest verifyCodeRequest) {
        String storedCode = stringRedisTemplate.opsForValue().get(verifyCodeRequest.email());

        if (storedCode != null && storedCode.equals(verifyCodeRequest.verificationCode())) {
            // 인증 코드가 일치하는 경우, 사용자 정보를 업데이트하고 인증 코드를 Redis에서 삭제
            userRepository.findByUserId(verifyCodeRequest.userPk())
                    .ifPresent(userEntity -> userEntity.emailUpdate(verifyCodeRequest.email()));
            stringRedisTemplate.delete(verifyCodeRequest.email()); // 인증 코드 삭제
            return true;
        } else {
            return false;
        }
    }
}
