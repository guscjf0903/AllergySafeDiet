package org.api.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.api.entity.FoodEntity;
import org.api.entity.HealthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthRepository extends JpaRepository<HealthEntity, Long> {
    Optional<HealthEntity> getHealthDataByHealthDateAndUserUserId(LocalDate date, Long userId);
    void deleteHealthDataByHealthDateAndUserUserId(LocalDate date, Long userId);

    Optional<List<HealthEntity>> findByHealthRecordIdIn(List<Long> ids);

}

