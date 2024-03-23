package org.core.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.core.dto.IngredientsRequest;

public record FoodResponse (
        long id,
        LocalDate date,
        String mealType,
        LocalTime mealTime,
        String foodName,
        List<IngredientsRequest> ingredients,
        String foodNotes
){
    public static FoodResponse toResponse(long id, LocalDate date, String mealType, LocalTime mealTime, String foodName, List<IngredientsRequest> ingredients, String notes) {
        return new FoodResponse(id, date, mealType, mealTime, foodName, ingredients, notes);
    }
}
