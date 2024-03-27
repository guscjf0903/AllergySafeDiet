package org.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.api.openapi.IngredientResponse;
import org.api.service.OpenApiClient;
import org.api.openapi.RecipeResponse;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecipeApiClient {

    private final OpenApiProperties openApiProperties;
    private final OpenApiClient openApiClient;
    private final ObjectMapper objectMapper;

//    public int getRecipeIdByName(String foodName) throws IOException {
//        String jsonString = openApiClient.callRecipeApi(openApiProperties.getRecipeInfoPath(), "RECIPE_NM_KO",
//                foodName);
//        JsonNode rootNode = objectMapper.readTree(jsonString);
//        JsonNode recipeIdNode = rootNode.findValue("RECIPE_ID");
//
//        return Optional.ofNullable(recipeIdNode)
//                .filter(JsonNode::canConvertToInt)
//                .map(JsonNode::intValue)
//                .orElseThrow(() -> new CustomException(NOT_FOUND_RECIPE));
//    }

    public int getRecipeIdByName(String foodName) throws IOException {
        String jsonString = openApiClient.callRecipeApi(openApiProperties.getRecipeInfoPath(), "RECIPE_NM_KO", foodName);
        RecipeResponse response = objectMapper.readValue(jsonString, RecipeResponse.class);
        System.out.println(response.getRecipeId());

        return response.getRecipeId();
    }

    public List<String> getIngredientsByRecipeId(int recipeId) throws IOException {
        String jsonString = openApiClient.callRecipeApi(openApiProperties.getRecipeDetailsPath(), "RECIPE_ID", recipeId);
        ObjectMapper objectMapper = new ObjectMapper();
        IngredientResponse response = objectMapper.readValue(jsonString, IngredientResponse.class);

        return response.getIngredientNames();
    }


//    public int getRecipeIdByName(String foodName) throws IOException {
//        String jsonString = openApiClient.callRecipeApi(openApiProperties.getRecipeInfoPath(), "RECIPE_NM_KO", foodName);
//        RecipeIdResponse response = objectMapper.readValue(jsonString, RecipeIdResponse.class);
//
//        return Optional.of(response.RECIPE_ID())
//                .orElseThrow(() -> new CustomException(NOT_FOUND_RECIPE));
//    } //{"Grid_20150827000000000226_1":{"totalCnt":1,"startRow":1,"endRow":5,"result":{"code":"INFO-000","message":"정상 처리되었습니다."},"row":[{"ROW_NUM":1,"RECIPE_ID":445,"RECIPE_NM_KO":"된장찌개","SUMRY":"구수한 냄새, 고소한 맛! 한국의 대표음식 된장찌개! 우리 입맛에 딱이예요~","NATION_CODE":"3020001","NATION_NM":"한식","TY_CODE":"3010017","TY_NM":"찌개/전골/스튜","COOKING_TIME":"30분","CALORIE":"136Kcal","QNT":"2인분","LEVEL_NM":"초보환영","IRDNT_CODE":"","PC_NM":""}]}}

//    public List<String> getIngredientsByRecipeId(int recipeId) throws IOException {
//        String jsonString = openApiClient.callRecipeApi(openApiProperties.getRecipeDetailsPath(), "RECIPE_ID",
//                recipeId);
//        System.out.println(jsonString);
//        JsonNode rootNode = objectMapper.readTree(jsonString);
//
//        return rootNode.findValues("IRDNT_NM").stream()
//                .map(JsonNode::asText)
//                .collect(Collectors.toList());
//    }

}