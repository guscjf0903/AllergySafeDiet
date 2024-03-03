package org.api.service;

import static org.api.exception.ErrorCodes.NOT_FOUND_LOGINID;
import static org.api.exception.ErrorCodes.NOT_FOUND_USER;
import static org.api.exception.ErrorCodes.PASSWORD_DISMATCH;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.api.entity.LoginEntity;
import org.api.entity.UserEntity;
import org.api.exception.CustomException;
import org.api.repository.LoginRepository;
import org.api.repository.UserRepository;
import org.core.dto.LoginDto;
import org.core.response.LoginResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final LoginRepository loginRepository;
    private final UserRepository userRepository;

    @Transactional
    public LoginResponse loginUser(LoginDto loginDto) {
        UserEntity userEntity = userRepository.findByUserName(loginDto.getLoginId())
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        if (!userEntity.getPassword().equals(loginDto.getLoginPassword())) {
            throw new CustomException(PASSWORD_DISMATCH);
        } //아이디 비밀번호 검증

        loginRepository.deleteByUserUserId(userEntity.getUserId()); //기존 로그인 정보가 있으면 삭제
        LoginEntity loginEntity = onSuccessfulLogin(userEntity);

        return new LoginResponse(loginEntity.getLoginToken());
    }

    private LoginEntity onSuccessfulLogin(UserEntity userEntity) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expirationTime = LocalDateTime.now().plusHours(1);

        LoginEntity loginEntity = LoginEntity.of(userEntity, token, expirationTime);

        return loginRepository.save(loginEntity);
    }

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void cleanExpiredLogin() {
        loginRepository.deleteAllByTokenExpirationTimeBefore(LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public LoginEntity validateLoginId(String loginToken) {
        if (loginToken == null) {
            throw new CustomException(NOT_FOUND_LOGINID);
        }
        return loginRepository.findByLoginToken(loginToken)
                .orElseThrow(() -> new CustomException(NOT_FOUND_LOGINID));
    }

}
