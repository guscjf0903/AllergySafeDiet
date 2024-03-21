package org.api.config;

import static org.api.exception.ErrorCodes.NOT_FOUND_RECIPE;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.api.exception.CustomException;
import org.api.service.OpenApiClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecipeApiClient {

    private final OpenApiProperties openApiProperties;
    private final OpenApiClient openApiClient;
    private final ObjectMapper objectMapper;

    public int getRecipeIdByName(String foodName) throws IOException {
        String response = openApiClient.callRecipeApi(openApiProperties.getRecipeInfoPath(), "RECIPE_NM_KO", foodName);
        return recipeIdExtractor(response);
    }

    public List<String> getIngredientsByRecipeId(int recipeId) throws IOException {
        String response = openApiClient.callRecipeApi(openApiProperties.getRecipeDetailsPath(), "RECIPE_ID", recipeId);
        return extractIngredientNames(response);
    }

    private int recipeIdExtractor(String jsonString) throws IOException {
        JsonNode rootNode = objectMapper.readTree(jsonString);
        JsonNode recipeIdNode = rootNode.findValue("RECIPE_ID"); // 특정 키를 가진 첫 번째 노드를 찾음

        if (recipeIdNode != null && recipeIdNode.canConvertToInt()) {
            return recipeIdNode.intValue();
        } else {
            throw new CustomException(NOT_FOUND_RECIPE); // RECIPE_ID를 찾을 수 없는 경우 예외를 발생
        }
    }

    private List<String> extractIngredientNames(String jsonString) throws IOException {
        List<String> ingredientNames = new ArrayList<>();
        JsonNode rootNode = objectMapper.readTree(jsonString);

        // "IRDNT_NM" 키를 가진 모든 노드를 찾아 리스트로 반환
        List<JsonNode> ingredientNodes = rootNode.findValues("IRDNT_NM");

        for (JsonNode ingredientNode : ingredientNodes) {
            String ingredientName = ingredientNode.asText(); // JsonNode를 문자열로 변환
            ingredientNames.add(ingredientName); // DTO 리스트에 추가
        }
        return ingredientNames;
    }
}