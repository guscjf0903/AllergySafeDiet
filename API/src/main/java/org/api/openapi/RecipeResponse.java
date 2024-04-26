package org.api.openapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class RecipeResponse {
    @JsonProperty("Grid_20150827000000000226_1")
    private RecipeRoot root;

    @Data
    public static class RecipeRoot {
        private int totalCnt;
        private int startRow;
        private int endRow;

        @JsonProperty("result")
        private RecipeResult result;
        @JsonProperty("row")
        private List<Row> row;
    }

    @Data
    public static class RecipeResult {
        private String code;
        private String message;
    }

    @Data
    public static class Row {
        @JsonProperty("ROW_NUM")
        private int rowNum;

        @JsonProperty("RECIPE_ID")
        private int recipeId;

        @JsonProperty("CALORIE")
        private String calorie;

        @JsonProperty("NATION_NM")
        private String nationNm;
    }

}
