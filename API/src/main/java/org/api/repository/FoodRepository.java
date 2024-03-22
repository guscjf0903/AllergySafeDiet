package org.api.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.api.entity.FoodEntity;
import org.api.entity.HealthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<FoodEntity, Long> {
    Optional<List<FoodEntity>> getFoodDataByFoodDateAndUserUserId(LocalDate date, Long userId);
    Optional<FoodEntity> getFoodDataByFoodRecordIdAndUserUserId(Long id, Long userId);

}
