package org.core.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.core.dto.IngredientsDto;

public record FoodResponse (
        LocalDate date,
        String mealType,
        LocalTime mealTime,
        String foodName,
        List<IngredientsDto> ingredients,
        String foodNotes
){
    public static FoodResponse toResponse(LocalDate date, String mealType, LocalTime mealTime, String foodName, List<IngredientsDto> ingredients, String notes) {
        return new FoodResponse(date, mealType, mealTime, foodName, ingredients, notes);
    }
};
