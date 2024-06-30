package org.api.service;

import static org.api.exception.ErrorCodes.ALREADY_EMAIL;
import static org.api.exception.ErrorCodes.DUPLICATE_EMAIL;
import static org.api.exception.ErrorCodes.ERROR_CREATE_CODE;
import static org.api.exception.ErrorCodes.FAILED_MAIL_SEND;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.api.exception.CustomException;
import org.api.repository.UserRepository;
import org.api.util.EncryptionUtil;
import org.core.request.MailRequest;
import org.core.request.VerifyCodeRequest;
import org.springframework.http.HttpStatus;


import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.api.entity.UserEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class VerificationCodeService {
    private final MailService mailService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final StringRedisTemplate stringRedisTemplate;
    private final EncryptionUtil encryptionUtil;
    private final WebClient.Builder webClientBuilder;

    private static final String EMAIL_VERIFICATION_SUBJECT = "Email Verification";
    private static final String MAIL_SERVICE_URL = "http://localhost:6666/mail/send"; // 메일 모듈의 URL


    private static final Random random = new SecureRandom();



    public void sendCodeToEmail(String email) {
        if (userService.checkDuplicateMail(email)) { //이미 있는 이메일인지 확인
            throw new CustomException(DUPLICATE_EMAIL);
        }
        String code = createCodeAndSaveRedis(email);

        MailRequest mailRequest = new MailRequest(email, EMAIL_VERIFICATION_SUBJECT, "Your verification code is: " + code);

        // WebClient를 사용하여 메일 모듈로 요청 전송
        WebClient webClient = webClientBuilder.build();
        try {
            webClient.post()
                    .uri(MAIL_SERVICE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(mailRequest)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response ->
                            Mono.error(new CustomException(FAILED_MAIL_SEND)))
                    .onStatus(HttpStatusCode::is5xxServerError, response ->
                            Mono.error(new CustomException(FAILED_MAIL_SEND)))
                    .bodyToMono(Void.class)
                    .block();
        } catch (Exception e) {
            throw new CustomException(FAILED_MAIL_SEND);
        }
    }


    public String createCodeAndSaveRedis(String email) {
        int length = 6;
        try {
            String code = generateCode(length);
            stringRedisTemplate.opsForValue().set(email, code, Duration.ofMinutes(5));

            return code;
        } catch (Exception e) {
            throw new CustomException(ERROR_CREATE_CODE);
        }
    }

    private String generateCode(int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }


    @Transactional
    public boolean validateEmailCodeFromRedis(VerifyCodeRequest verifyCodeRequest) {
        String storedCode = stringRedisTemplate.opsForValue().get(verifyCodeRequest.email());
        if (storedCode == null || !storedCode.equals(verifyCodeRequest.verificationCode())) {
            return false;
        }

        return userRepository.findByUserId(Long.parseLong(encryptionUtil.decrypt(verifyCodeRequest.userPk())))
                .map(userEntity -> {
                    if (userEntity.isEmailVerified()) {
                        throw new CustomException(ALREADY_EMAIL);
                    }
                    userEntity.emailUpdate(verifyCodeRequest.email());
                    stringRedisTemplate.delete(verifyCodeRequest.email());
                    return true;
                }).orElse(false);
    }

}
