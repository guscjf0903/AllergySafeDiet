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

    public List<IngredientsDto> getFoodRecipes(String foodName) {
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

    private int recipeIdExtractor(String jsonString)
            throws IOException { //1번쨰 openapi에서 받아온 jsonString을 파싱하여 recipeId를 추출하는 메소드
        JsonNode rootNode = objectMapper.readTree(jsonString);
        JsonNode recipeIdNode = rootNode.findValue("RECIPE_ID"); // 특정 키를 가진 첫 번째 노드를 찾음

        if (recipeIdNode != null && recipeIdNode.canConvertToInt()) {
            return recipeIdNode.intValue();
        } else {
            throw new CustomException(NOT_FOUND_RECIPE); // RECIPE_ID를 찾을 수 없는 경우 예외를 발생
        }

    }

    private List<IngredientsDto> extractIngredientNames(String jsonString) throws IOException {
        List<IngredientsDto> ingredientNames = new ArrayList<>();
        JsonNode rootNode = objectMapper.readTree(jsonString);

        // "IRDNT_NM" 키를 가진 모든 노드를 찾아 리스트로 반환
        List<JsonNode> ingredientNodes = rootNode.findValues("IRDNT_NM");

        for (JsonNode ingredientNode : ingredientNodes) {
            String ingredientName = ingredientNode.asText(); // JsonNode를 문자열로 변환
            ingredientNames.add(new IngredientsDto(ingredientName)); // DTO 리스트에 추가
        }

        return ingredientNames;
    }

}
