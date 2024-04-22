package org.api.service;

import lombok.RequiredArgsConstructor;
import org.api.config.jwtsecurity.JwtConfig;
import org.api.entity.UserEntity;
import org.api.repository.UserRepository;
import org.api.util.EncryptionUtil;
import org.core.request.LoginRequest;
import org.core.response.LoginResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final AuthenticationManager authenticationManager;

    @Transactional
    public LoginResponse loginUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.loginId(),
                        loginRequest.loginPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtConfig.generateAccessToken(((UserEntity) authentication.getPrincipal()).getUserId());
        String refreshToken = jwtConfig.generateRefreshToken(((UserEntity) authentication.getPrincipal()).getUserId());

        return new LoginResponse(accessToken, refreshToken);
//        UserEntity userEntity = userRepository.findByUserName(loginRequest.loginId())
//                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
//
//        if (!bCryptPasswordEncoder.matches(loginRequest.loginPassword(), userEntity.getPassword())) {
//            throw new CustomException(PASSWORD_DISMATCH);
//        } else if (!userEntity.isEmailVerified()) {
//            Map<String, String> userPkData = new HashMap<>();
//            String userPk = encryptionUtil.encrypt(userEntity.getUserId().toString());
//            userPkData.put("userPk", userPk);
//            throw new CustomException(INVALID_EMAIL, userPkData);
//        }
//
//        return jwtConfig.generateRefreshToken(userEntity.getUserId());
    }
}
