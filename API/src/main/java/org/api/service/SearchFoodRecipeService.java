package org.api.service;

import static org.api.exception.ErrorCodes.NOT_FOUND_RECIPE;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.api.exception.CustomException;
import org.core.dto.IngredientsDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class SearchFoodRecipeService {
    @Value("${openApi.serviceKey}")
    private String serviceKey;
    @Value("${openApi.baseUrl}")
    private String baseUrl;

    public List<IngredientsDto> getFoodRecipes(String foodName) throws IOException {
        String RecipeIdJsonString = getRecipeIdByName(foodName);
        int recipeId = recipeIdExtractor(RecipeIdJsonString);

        String IngredientsJsonString = getRecipeById(recipeId);

        return extractIngredientNames(IngredientsJsonString);
    }

    private String getRecipeIdByName(String foodName) {
        URI requestUrl = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .pathSegment(serviceKey, "json", "Grid_20150827000000000226_1", "1", "5")
                .queryParam("RECIPE_NM_KO", foodName)
                .encode() // URI 인코딩 적용
                .build() // UriComponents 객체를 빌드
                .toUri(); // 인코딩된 URI 객체를 얻음

        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject(requestUrl, String.class);
    }

    private int recipeIdExtractor(String jsonString) throws IOException { //1번쨰 openapi에서 받아온 jsonString을 파싱하여 recipeId를 추출하는 메소드
        int recipeId = 0;
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonString);
        JsonNode rowsNode = rootNode.path("Grid_20150827000000000226_1").path("row");

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

    private String getRecipeById(int recipeId) {
        URI requestUrl = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .pathSegment(serviceKey, "json", "Grid_20150827000000000227_1", "1", "5")
                .queryParam("RECIPE_ID", recipeId)
                .encode() // URI 인코딩 적용
                .build() // UriComponents 객체를 빌드
                .toUri(); // 인코딩된 URI 객체를 얻음

        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject(requestUrl, String.class);
    }

    private List<IngredientsDto> extractIngredientNames(String jsonString) throws IOException {
        List<IngredientsDto> ingredientNames = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonString);
        JsonNode rowsNode = rootNode.path("Grid_20150827000000000227_1").path("row");

        if (rowsNode.isArray()) {
            for (JsonNode rowNode : rowsNode) {
                String ingredientName = rowNode.path("IRDNT_NM").asText();
                ingredientNames.add(new IngredientsDto(ingredientName));
            }
        }

        return ingredientNames;
    }
}
