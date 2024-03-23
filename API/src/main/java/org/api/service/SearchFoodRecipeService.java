package org.api.service;

import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.api.config.RecipeApiClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchFoodRecipeService {
    private final RecipeApiClient recipeApiClient;

    @Cacheable(value = "recipes", key = "#foodName")
    public List<String> getFoodRecipes(String foodName) {
        try {
            // foodName을 이용하여 레시피 ID를 조회합니다.
            int recipeId = recipeApiClient.getRecipeIdByName(foodName);
            // 레시피 ID를 이용하여 재료 목록을 조회합니다.

            return recipeApiClient.getIngredientsByRecipeId(recipeId);
        } catch (IOException e) {
            // 예외 처리: API 호출이 실패한 경우
            throw new RuntimeException("Error fetching recipe data for " + foodName, e);
        }
    }
}
