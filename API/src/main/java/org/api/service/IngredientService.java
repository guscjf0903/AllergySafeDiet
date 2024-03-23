package org.api.service;

import lombok.RequiredArgsConstructor;
import org.api.entity.IngredientEntity;
import org.api.entity.FoodEntity;
import org.api.repository.IngredientRepository;
import org.core.dto.FoodRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    @Transactional
    public void saveIngredient(FoodEntity foodEntity, FoodRequest foodRequest) {
        if (foodRequest.ingredients() == null) {
            return;
        }

        for (String ingredient : foodRequest.ingredients()) {
            IngredientEntity ingredientEntity = IngredientEntity.of(foodEntity, ingredient);
            ingredientRepository.save(ingredientEntity);
        }
    }

    @Transactional
    public void putIngredient(FoodEntity foodEntity, Long foodId, FoodRequest foodRequest) {
        if (foodRequest.ingredients() == null || foodRequest.ingredients().isEmpty()) {
            return;
        }

        ingredientRepository.deleteByFoodFoodRecordId(foodId);

        for (String ingredient : foodRequest.ingredients()) {
            IngredientEntity ingredientEntity = IngredientEntity.of(foodEntity, ingredient);
            ingredientRepository.save(ingredientEntity);
        }
    }

}
