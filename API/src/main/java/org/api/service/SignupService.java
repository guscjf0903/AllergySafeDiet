package org.api.service;

import static org.api.exception.ErrorCodes.INVALID_EMAIL;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.api.entity.UserEntity;
import org.api.exception.CustomException;
import org.api.repository.UserRepository;
import org.core.request.SignupRequest;
import org.core.response.SignupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupService {
    private final UserRepository userRepository;

    @Transactional
    public Optional<SignupResponse> registerUser(SignupRequest signupRequest) {
        UserEntity user = UserEntity.of(signupRequest);
        SignupResponse signupResponse = SignupResponse.toResponse(userRepository.save(user).getUserId());

        return Optional.of(signupResponse);
    }



    @Transactional(readOnly = true)
    public boolean checkDuplicateMail(String email) {
        return userRepository.existsByEmail(email);
    }
}

