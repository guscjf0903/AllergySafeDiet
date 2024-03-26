package org.api.service;

import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.api.config.RecipeApiClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchFoodRecipeService {
    private final RecipeApiClient recipeApiClient;

    @Cacheable(value = "recipes", key = "#foodName")
    public List<String> getFoodRecipes(String foodName) {
        try {
            int recipeId = recipeApiClient.getRecipeIdByName(foodName);

            return recipeApiClient.getIngredientsByRecipeId(recipeId);
        } catch (IOException e) {
            throw new RuntimeException("Error fetching recipe data for " + foodName, e);
        }
    }
}
