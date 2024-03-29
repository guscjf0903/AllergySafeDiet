package org.api.service;

import static org.api.exception.ErrorCodes.NOT_FOUND_LOGINID;

import lombok.RequiredArgsConstructor;
import org.api.entity.UserEntity;
import org.api.exception.CustomException;
import org.api.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserEntity loadUserByUsername(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new CustomException(NOT_FOUND_LOGINID));
    }

    public UserEntity loadUserById(Long userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_LOGINID));
    }
}