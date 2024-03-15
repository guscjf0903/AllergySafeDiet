package org.api.repository;

import java.time.LocalDate;
import java.util.Optional;
import org.api.entity.HealthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthRepository extends JpaRepository<HealthEntity, Long> {
    Optional<HealthEntity> getHealthDataByHealthDateAndUserUserId(LocalDate date, Long userId);
}

