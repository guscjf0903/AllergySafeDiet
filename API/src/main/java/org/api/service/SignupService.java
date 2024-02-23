package org.api.service;

import static org.api.exception.ErrorCodes.USERSIGNUP_FAILED;

import lombok.RequiredArgsConstructor;
import org.api.entity.UserEntity;
import org.api.exception.CustomException;
import org.api.repository.UserRepository;
import org.core.dto.SignupDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupService {
    private final UserRepository userRepository;

    @Transactional
    public void registerUser(SignupDto signupDTO) {
        try{
            UserEntity user = UserEntity.of(signupDTO);
            userRepository.save(user);
        } catch (Exception e) {
            throw new CustomException(USERSIGNUP_FAILED);
        }
    }
}

