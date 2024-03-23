package org.core.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
public record FoodRequest(
        LocalDate date,
        @NotBlank(message = "식사 종류를 입력해주세요.")
        String mealType,
        LocalTime mealTime,
        @NotBlank(message = "음식 이름을 입력해주세요.")
        String foodName,
        List<String> ingredients,
        String notes
) {}
