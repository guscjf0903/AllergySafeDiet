package org.core.dto;

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
    private LocalDate postDate;
    private String foodType;
    private LocalTime foodTime;
    private String foodName;
    private List<String> ingredients;
    private String notes;
    private int loginToken;

}
