package org.api.repository;

import java.time.LocalDateTime;
import org.api.entity.LoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<LoginEntity, Long> {
    void deleteByUserUserId(long loginId);
    void deleteAllByTokenExpirationTimeBefore(LocalDateTime now);
}
