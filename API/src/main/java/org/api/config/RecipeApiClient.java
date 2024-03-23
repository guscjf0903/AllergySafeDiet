package org.api.config;

import static org.api.exception.ErrorCodes.NOT_FOUND_RECIPE;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
    // JsonNode 처리를 위한 유틸리티 메서드 추가

    public int getRecipeIdByName(String foodName) throws IOException {
        String jsonString = openApiClient.callRecipeApi(openApiProperties.getRecipeInfoPath(), "RECIPE_NM_KO", foodName);
        JsonNode rootNode = objectMapper.readTree(jsonString);
        JsonNode recipeIdNode = rootNode.findValue("RECIPE_ID");

        return Optional.ofNullable(recipeIdNode)
                .filter(JsonNode::canConvertToInt)
                .map(JsonNode::intValue)
                .orElseThrow(() -> new CustomException(NOT_FOUND_RECIPE));
    }

    public List<String> getIngredientsByRecipeId(int recipeId) throws IOException {
        String jsonString = openApiClient.callRecipeApi(openApiProperties.getRecipeDetailsPath(), "RECIPE_ID", recipeId);
        JsonNode rootNode = objectMapper.readTree(jsonString);

        return rootNode.findValues("IRDNT_NM").stream()
                .map(JsonNode::asText)
                .collect(Collectors.toList());
    }

}