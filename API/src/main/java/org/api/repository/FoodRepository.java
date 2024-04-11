package org.api.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.api.entity.FoodEntity;
import org.api.entity.HealthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<FoodEntity, Long> {
    Optional<List<FoodEntity>> getFoodDataByFoodDateAndUserUserId(LocalDate date, Long userId);
    Optional<FoodEntity> getFoodDataByFoodRecordIdAndUserUserId(Long id, Long userId);

    void deleteByFoodRecordId(Long id);

    Optional<List<FoodEntity>> findByFoodRecordIdIn(List<Long> ids);

}
