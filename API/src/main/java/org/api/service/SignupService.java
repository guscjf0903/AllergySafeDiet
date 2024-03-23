package org.api.service;

import static org.api.exception.ErrorCodes.INVALID_EMAIL;

import lombok.RequiredArgsConstructor;
import org.api.entity.UserEntity;
import org.api.exception.CustomException;
import org.api.repository.UserRepository;
import org.core.request.SignupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupService {
    private final UserRepository userRepository;

    @Transactional
    public void registerUser(SignupRequest signupRequest) {
        checkVerificationMail(signupRequest.checkVerificationEmail());
        UserEntity user = UserEntity.of(signupRequest);
        userRepository.save(user);
    }

    private void checkVerificationMail(boolean checkVerificationEmail) { //이메일 인증 확인
        if (!checkVerificationEmail) {
            throw new CustomException(INVALID_EMAIL);
        }
    }


    @Transactional(readOnly = true)
    public boolean checkDuplicateMail(String email) {
        return userRepository.existsByEmail(email);
    }
}

