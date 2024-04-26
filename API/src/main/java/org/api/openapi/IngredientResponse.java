package org.api.openapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class IngredientResponse {

    @JsonProperty("Grid_20150827000000000227_1")
    private IngredientRoot root;

    @Data
    public static class IngredientRoot {
        private int totalCnt;
        private int startRow;
        private int endRow;
        @JsonProperty("result")
        private IngredientResult result;
        @JsonProperty("row")
        private List<IngredientRow> row;
    }

    @Data
    public static class IngredientResult {
        private String code;
        private String message;
    }

    @Data
    public static class IngredientRow {
        @JsonProperty("ROW_NUM")
        private int rowNum;
        @JsonProperty("RECIPE_ID")
        private int recipeId;
        @JsonProperty("IRDNT_SN")
        private int irdntSn;
        @JsonProperty("IRDNT_NM")
        private String irdntNm;
        @JsonProperty("IRDNT_CPCTY")
        private String irdntCpcty;
        @JsonProperty("IRDNT_TY_CODE")
        private String irdntTyCode;
        @JsonProperty("IRDNT_TY_NM")
        private String irdntTyNm;
    }
}

