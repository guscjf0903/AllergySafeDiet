package org.core.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuDto {
    private LocalDate date;
    @NotBlank(message = "식사 종류를 입력해주세요.")
    private String mealType;
    private LocalTime mealTime;
    @NotBlank(message = "음식 이름을 입력해주세요.")
    private String foodName;
    private List<String> ingredients;
    private String notes;
    private Long loginToken;

}
