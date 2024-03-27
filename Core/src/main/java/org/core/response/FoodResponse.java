package org.core.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record FoodResponse (
        long id,
        LocalDate date,
        String mealType,
        LocalTime mealTime,
        String foodName,
        List<IngredientsResponse> ingredients,
        String foodNotes
){
    public static FoodResponse toResponse(long id, LocalDate date, String mealType, LocalTime mealTime, String foodName, List<IngredientsResponse> ingredients, String notes) {
        return new FoodResponse(id, date, mealType, mealTime, foodName, ingredients, notes);
    }
}
