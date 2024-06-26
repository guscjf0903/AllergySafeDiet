package org.api.repository;

import java.util.Optional;
import org.api.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserId(Long userId);
    Optional<UserEntity> findByUserName(String username);
    boolean existsByEmail(String email);
}