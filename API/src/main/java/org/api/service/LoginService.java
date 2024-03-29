package org.api.service;

import static org.api.exception.ErrorCodes.INVALID_EMAIL;
import static org.api.exception.ErrorCodes.NOT_FOUND_LOGINID;
import static org.api.exception.ErrorCodes.NOT_FOUND_USER;
import static org.api.exception.ErrorCodes.PASSWORD_DISMATCH;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.api.config.JwtConfig;
import org.api.entity.LoginEntity;
import org.api.entity.UserEntity;
import org.api.exception.CustomException;
import org.api.repository.LoginRepository;
import org.api.repository.UserRepository;
import org.core.request.LoginRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final LoginRepository loginRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtConfig jwtConfig;


//    @Transactional
//    public LoginResponse loginUser(LoginRequest loginRequest) {
//        UserEntity userEntity = userRepository.findByUserName(loginRequest.loginId())
//                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
//        if (!userEntity.getPassword().equals(loginRequest.loginPassword())) {
//            throw new CustomException(PASSWORD_DISMATCH);
//        } else if(!userEntity.isEmailVerified()) {
//            Map<String, Long> userPkData = new HashMap<>();
//            userPkData.put("userPk", userEntity.getUserId());
//            throw new CustomException(INVALID_EMAIL, userPkData);
//        }
//
//        loginRepository.deleteByUserUserId(userEntity.getUserId()); //기존 로그인 정보가 있으면 삭제
//        LoginEntity loginEntity = onSuccessfulLogin(userEntity);
//
//        return new LoginResponse(loginEntity.getLoginToken());
//    }

    @Transactional
    public String loginUser(LoginRequest loginRequest) {
        UserEntity userEntity = userRepository.findByUserName(loginRequest.loginId())
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        if (!bCryptPasswordEncoder.matches(loginRequest.loginPassword(), userEntity.getPassword())) {
            throw new CustomException(PASSWORD_DISMATCH);
        } else if (!userEntity.isEmailVerified()) {
            Map<String, Long> userPkData = new HashMap<>();
            userPkData.put("userPk", userEntity.getUserId());
            throw new CustomException(INVALID_EMAIL, userPkData);
        }

        return jwtConfig.generateToken(userEntity.getUserId());
    }

//    private LoginEntity onSuccessfulLogin(UserEntity userEntity) {
//        String token = JwtConfig.generateToken(userEntity.getUsername());
//        LocalDateTime expirationTime = LocalDateTime.now().plusHours(1);
//
//        LoginEntity loginEntity = LoginEntity.of(userEntity, token, expirationTime);
//
//        return loginRepository.save(loginEntity);
//    }

//    @Scheduled(fixedRate = 3600000)
//    @Transactional
//    public void cleanExpiredLogin() {
//        loginRepository.deleteAllByTokenExpirationTimeBefore(LocalDateTime.now());
//    }

    @Transactional(readOnly = true)
    public LoginEntity validateLoginId(String loginToken) {
        return loginRepository.findByLoginToken(loginToken)
                .orElseThrow(() -> new CustomException(NOT_FOUND_LOGINID));
    }

}
