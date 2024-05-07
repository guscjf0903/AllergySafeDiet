package org.api.service;

import lombok.RequiredArgsConstructor;
import org.api.entity.UserEntity;
import org.api.exception.EmailNotVerifiedException;
import org.api.repository.UserRepository;
import org.api.util.EncryptionUtil;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserEntity loadUserByUsername(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userName));
    }
}
