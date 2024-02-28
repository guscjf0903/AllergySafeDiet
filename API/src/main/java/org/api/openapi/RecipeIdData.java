package org.api.openapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeIdData {
    @JsonProperty("RECIPE_ID")
    private int recipeId;
}
