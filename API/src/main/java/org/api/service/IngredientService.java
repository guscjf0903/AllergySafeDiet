package org.api.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.api.entity.IngredientEntity;
import org.api.entity.FoodEntity;
import org.api.repository.IngredientRepository;
import org.core.dto.MenuDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    @Transactional
    public void saveIngredient(FoodEntity foodEntity, MenuDto menuDto) {
        if(menuDto.ingredients() == null) return;

        for(String ingredient : menuDto.ingredients()) {
            IngredientEntity ingredientEntity = IngredientEntity.of(foodEntity, ingredient);
            ingredientRepository.save(ingredientEntity);
        }
    }
    
    @Transactional
    public void putIngredient(FoodEntity foodEntity, Long foodId, MenuDto menuDto) {
        if(menuDto.ingredients() == null || menuDto.ingredients().isEmpty()) return;

        ingredientRepository.deleteByFoodFoodRecordId(foodId);

        for(String ingredient : menuDto.ingredients()) {
            IngredientEntity ingredientEntity = IngredientEntity.of(foodEntity, ingredient);
            ingredientRepository.save(ingredientEntity);
        }
    }

}
