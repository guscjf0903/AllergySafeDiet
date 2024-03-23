package org.api.service;

import lombok.RequiredArgsConstructor;
import org.api.entity.IngredientEntity;
import org.api.entity.FoodEntity;
import org.api.repository.IngredientRepository;
import org.core.request.FoodRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    @Transactional
    public void saveIngredientData(FoodEntity foodEntity, FoodRequest foodRequest) {
        foodRequest.ingredients().forEach(ingredient ->
                ingredientRepository.save(IngredientEntity.of(foodEntity, ingredient))
        );
    }

    @Transactional
    public void putIngredientData(FoodEntity foodEntity, FoodRequest foodRequest) {
        ingredientRepository.deleteByFoodFoodRecordId(foodEntity.getFoodRecordId());
        saveIngredientData(foodEntity, foodRequest);
    }
}
