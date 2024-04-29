package org.api.service;

import static org.api.exception.ErrorCodes.INVALID_EMAIL;
import static org.api.exception.ErrorCodes.NOT_FOUND_LOGINID;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.api.entity.UserEntity;
import org.api.exception.CustomException;
import org.api.repository.UserRepository;
import org.api.util.EncryptionUtil;
import org.core.request.SignupRequest;
import org.core.response.SignupResponse;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EncryptionUtil encryptionUtil;

    @Transactional
    public Optional<SignupResponse> registerUser(SignupRequest signupRequest){
        String encodedPassword = bCryptPasswordEncoder.encode(signupRequest.password());

        UserEntity user = UserEntity.builder()
                .userName(signupRequest.userName())
                .password(encodedPassword)
                .birthDate(signupRequest.birthDate())
                .gender(signupRequest.gender())
                .height(signupRequest.height())
                .build();

        String encryptUserPk = encryptionUtil.encrypt(userRepository.save(user).getUserId().toString());
        SignupResponse signupResponse = SignupResponse.toResponse(encryptUserPk);

        return Optional.of(signupResponse);
    }

    @Transactional(readOnly = true)
    public boolean checkDuplicateMail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public UserEntity loadUserById(Long userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_LOGINID));
    }
}

