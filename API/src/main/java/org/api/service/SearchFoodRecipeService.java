package org.api.service;

import static org.api.exception.ErrorCodes.NOT_FOUND_RECIPE;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
        return getRecipesRedisCache(foodName)
                .orElseGet(() -> fetchAndCacheFoodRecipes(foodName));
    }

    private Optional<List<IngredientsDto>> getRecipesRedisCache(String foodName) {
        return foodRecipeRedisRepository.findById(foodName)
                .map(entity -> entity.getIngredients().stream()
                        .map(IngredientsDto::new)
                        .collect(Collectors.toList()));
    }

    private List<IngredientsDto> fetchAndCacheFoodRecipes(String foodName) {
        try {
            int recipeId = getRecipeIdByName(foodName);
            List<IngredientsDto> ingredients = getIngredientsByRecipeId(recipeId);

            foodRecipeRedisRepository.save(new FoodRecipeRedisEntity(foodName, ingredients.stream()
                    .map(IngredientsDto::ingredientName)
                    .collect(Collectors.toList())
            ));
            return ingredients;

        } catch (IOException e) {
            throw new CustomException(NOT_FOUND_RECIPE);
        }
    }


    private int getRecipeIdByName(String foodName) throws IOException {
        String response = openApiClient.callRecipeApi(openApiProperties.getRecipeInfoPath(), "RECIPE_NM_KO", foodName);
        return recipeIdExtractor(response);
    }

    private List<IngredientsDto> getIngredientsByRecipeId(int recipeId) throws IOException {
        String response = openApiClient.callRecipeApi(openApiProperties.getRecipeDetailsPath(), "RECIPE_ID", recipeId);
        return extractIngredientNames(response);
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
//
//    private List<IngredientsDto> fetchAndCacheFoodRecipes(String foodName) {
//        try {
//            int recipeId = getRecipeIdByName(foodName);
//            List<IngredientsDto> ingredients = getIngredientsByRecipeId(recipeId);
//
//            foodRecipeRedisRepository.save(new FoodRecipeRedisEntity(foodName, ingredients.stream()
//                    .map(IngredientsDto::getIngredientName)
//                    .collect(Collectors.toList())));
//            return ingredients;
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to fetch or cache food recipes", e);
//        }
//    }
//
//    public List<IngredientsDto> getFoodRecipes(String foodName) throws IOException {
//        List<IngredientsDto> redisIngredients = getRecipesRedisCache(foodName);
//        if(redisIngredients != null) {
//            return redisIngredients;
//        }
//
//        String RecipeIdJsonString = openApiClient.callRecipeApi(openApiProperties.getRecipeInfoPath(), "RECIPE_NM_KO", foodName);
//        int recipeId = recipeIdExtractor(RecipeIdJsonString);
//
//        String IngredientsJsonString = openApiClient.callRecipeApi(openApiProperties.getRecipeDetailsPath(), "RECIPE_ID", recipeId);
//        List<IngredientsDto> apiIngredients = extractIngredientNames(IngredientsJsonString);
//        foodRecipeRedisRepository.save(new FoodRecipeRedisEntity(foodName, apiIngredients)); //redis에 저장
//
//        return apiIngredients;
//    }
//
//    private List<IngredientsDto> getRecipesRedisCache(String foodName) {
//        Optional<FoodRecipeRedisEntity> cachedRecipe = foodRecipeRedisRepository.findById(foodName);
//        return cachedRecipe.map(FoodRecipeRedisEntity::getIngredients).orElse(null);
//    }
//

}
