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
import org.api.util.EncryptionUtil;
import org.core.request.LoginRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtConfig jwtConfig;
    private final EncryptionUtil encryptionUtil;
    @Transactional
    public String loginUser(LoginRequest loginRequest) {
        UserEntity userEntity = userRepository.findByUserName(loginRequest.loginId())
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        if (!bCryptPasswordEncoder.matches(loginRequest.loginPassword(), userEntity.getPassword())) {
            throw new CustomException(PASSWORD_DISMATCH);
        } else if (!userEntity.isEmailVerified()) {
            Map<String, String> userPkData = new HashMap<>();
            String userPk = encryptionUtil.encrypt(userEntity.getUserId().toString());
            userPkData.put("userPk", userPk);
            throw new CustomException(INVALID_EMAIL, userPkData);
        }

        return jwtConfig.generateToken(userEntity.getUserId());
    }
}
