package org.api.service;

import static org.api.exception.ErrorCodes.NOT_FOUND_LOGINID;
import static org.api.exception.ErrorCodes.NOT_FOUND_USER;
import static org.api.exception.ErrorCodes.PASSWORD_DISMATCH;

import lombok.RequiredArgsConstructor;
import org.api.entity.LoginEntity;
import org.api.entity.UserEntity;
import org.api.exception.CustomException;
import org.api.repository.LoginRepository;
import org.api.repository.UserRepository;
import org.core.dto.LoginDto;
import org.core.response.LoginResponse;
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
        }

        loginRepository.deleteByUserUserId(userEntity.getUserId()); //기존 로그인 정보가 있으면 삭제

        LoginEntity loginEntity = LoginEntity.of(userEntity);
        loginRepository.save(loginEntity);

        return new LoginResponse(loginEntity.getLoginId());
    }

    @Transactional(readOnly = true)
    public LoginEntity validateLoginId(Long loginToken) {
        return loginRepository.findById(loginToken)
                .orElseThrow(() -> new CustomException(NOT_FOUND_LOGINID));
    }

}
