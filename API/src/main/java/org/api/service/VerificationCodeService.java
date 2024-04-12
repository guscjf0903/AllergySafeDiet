package org.api.service;

import static org.api.exception.ErrorCodes.ALREADY_EMAIL;
import static org.api.exception.ErrorCodes.DUPLICATE_EMAIL;
import static org.api.exception.ErrorCodes.ERROR_CREATE_CODE;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.api.exception.CustomException;
import org.api.repository.UserRepository;
import org.api.util.EncryptionUtil;
import org.core.request.VerifyCodeRequest;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.api.entity.UserEntity;

@RequiredArgsConstructor
@Service
public class VerificationCodeService {
    private final MailService mailService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final StringRedisTemplate stringRedisTemplate;
    private final EncryptionUtil encryptionUtil;


    private static final String EMAIL_VERIFICATION_SUBJECT = "Email Verification";

    public void sendCodeToEmail(String email) {
        if (userService.checkDuplicateMail(email)) {
            throw new CustomException(DUPLICATE_EMAIL);
        }
        String code = createCodeAndSaveRedis(email);
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
        String stringUserPk = encryptionUtil.decrypt(verifyCodeRequest.userPk());
        Long userPk = Long.parseLong(stringUserPk);

        Optional<UserEntity> userOptional = userRepository.findByUserId(userPk);

        userOptional.filter(UserEntity::isEmailVerified)
                .ifPresent(userEntity -> {
                    throw new CustomException(ALREADY_EMAIL);
                });

        if (storedCode != null && storedCode.equals(verifyCodeRequest.verificationCode())) {
            userOptional.ifPresent(userEntity -> {
                userEntity.emailUpdate(verifyCodeRequest.email());
            });
            stringRedisTemplate.delete(verifyCodeRequest.email());
            return true;
        }

        return false;
    }
}
