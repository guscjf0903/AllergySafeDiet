package org.api.service;

import static org.api.exception.ErrorCodes.NOT_FOUND_RECIPE;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.api.config.OpenApiProperties;
import org.api.entity.redis_entity.FoodRecipeRedisEntity;
import org.api.exception.CustomException;
import org.api.repository.redis_repository.FoodRecipeRedisRepository;
import org.core.dto.IngredientsDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchFoodRecipeService {
    private final OpenApiProperties openApiProperties;
    private final OpenApiClient openApiClient;
    private final ObjectMapper objectMapper;
    private final FoodRecipeRedisRepository foodRecipeRedisRepository;


    public List<IngredientsDto> getFoodRecipes(String foodName) throws IOException {
        List<IngredientsDto> redisIngredients = getRecipesRedisCache(foodName);
        if(redisIngredients != null) {
            return redisIngredients;
        }

        String RecipeIdJsonString = openApiClient.callRecipeApi(openApiProperties.getRecipeInfoPath(), "RECIPE_NM_KO", foodName);
        int recipeId = recipeIdExtractor(RecipeIdJsonString);

        String IngredientsJsonString = openApiClient.callRecipeApi(openApiProperties.getRecipeDetailsPath(), "RECIPE_ID", recipeId);
        List<IngredientsDto> apiIngredients = extractIngredientNames(IngredientsJsonString);
        foodRecipeRedisRepository.save(new FoodRecipeRedisEntity(foodName, apiIngredients)); //redis에 저장

        return apiIngredients;
    }

    private List<IngredientsDto> getRecipesRedisCache(String foodName) {
        Optional<FoodRecipeRedisEntity> cachedRecipe = foodRecipeRedisRepository.findById(foodName);
        return cachedRecipe.map(FoodRecipeRedisEntity::getIngredients).orElse(null);
    }

    private int recipeIdExtractor(String jsonString) throws IOException { //1번쨰 openapi에서 받아온 jsonString을 파싱하여 recipeId를 추출하는 메소드
        int recipeId = 0;
        JsonNode rootNode = objectMapper.readTree(jsonString);
        JsonNode rowsNode = rootNode.path(openApiProperties.getRecipeInfoPath()).path("row");

        if (rowsNode.isArray()) {
            for (JsonNode rowNode : rowsNode) {
                if (rowNode.has("RECIPE_ID")) {
                    recipeId = rowNode.get("RECIPE_ID").intValue();
                }
            }
        }
        if(recipeId == 0) {
            throw new CustomException(NOT_FOUND_RECIPE);
        }

        return recipeId;
    }

    private List<IngredientsDto> extractIngredientNames(String jsonString) throws IOException {
        List<IngredientsDto> ingredientNames = new ArrayList<>();
        JsonNode rootNode = objectMapper.readTree(jsonString);
        JsonNode rowsNode = rootNode.path(openApiProperties.getRecipeDetailsPath()).path("row");

        if (rowsNode.isArray()) {
            for (JsonNode rowNode : rowsNode) {
                String ingredientName = rowNode.path("IRDNT_NM").asText();
                ingredientNames.add(new IngredientsDto(ingredientName));
            }
        }

        return ingredientNames;
    }
}
