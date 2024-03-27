package org.api.openapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IngredientResponse {
    private List<String> ingredientNames = new ArrayList<>();

    @SuppressWarnings("unchecked")
    @JsonProperty("Grid_20150827000000000227_1")
    private void unpackNested(Map<String,Object> grid) {
        List<Map<String,Object>> rows = (List<Map<String,Object>>)grid.get("row");
        if (rows != null) {
            for (Map<String, Object> row : rows) {
                String ingredientName = (String)row.get("IRDNT_NM");
                ingredientNames.add(ingredientName);
            }
        }
    }
}
