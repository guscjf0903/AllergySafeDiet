package org.api.openapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeResponse {
    private int recipeId;
    @SuppressWarnings("unchecked")
    @JsonProperty("Grid_20150827000000000226_1")
    private void unpackNested(Map<String,Object> grid) {
        List<Map<String,Object>> rows = (List<Map<String,Object>>)grid.get("row");
        if (!rows.isEmpty()) {
            Map<String, Object> firstRow = rows.get(0);
            this.recipeId = (Integer)firstRow.get("RECIPE_ID");
        }
    }
}
