package org.api.config;

import static org.api.exception.ErrorCodes.NOT_FOUND_RECIPE;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import javax.swing.undo.CannotUndoException;
import lombok.RequiredArgsConstructor;
import org.api.exception.CustomException;
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

    public int getRecipeIdByName(String foodName) throws IOException {
        String jsonString = openApiClient.callRecipeApi(openApiProperties.getRecipeInfoPath(), "RECIPE_NM_KO",
                foodName);
        RecipeResponse response = objectMapper.readValue(jsonString, RecipeResponse.class);
        if (response.getRecipeId() == 0) {
            throw new CustomException(NOT_FOUND_RECIPE);
        }

        return response.getRecipeId();
    }

    public List<String> getIngredientsByRecipeId(int recipeId) throws IOException {
        String jsonString = openApiClient.callRecipeApi(openApiProperties.getRecipeDetailsPath(), "RECIPE_ID",
                recipeId);
        IngredientResponse response = objectMapper.readValue(jsonString, IngredientResponse.class);
        System.out.println(jsonString);

        return response.getIngredientNames();
    }


}