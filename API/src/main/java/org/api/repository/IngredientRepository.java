package org.api.repository;

import org.api.entity.IngredientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<IngredientEntity, Long> {
    void deleteByFoodFoodRecordId(Long id);
}
