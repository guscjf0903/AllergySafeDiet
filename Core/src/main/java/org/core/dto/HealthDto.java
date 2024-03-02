package org.core.dto;

import jakarta.validation.constraints.Email;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HealthDto {
    private LocalDate date;
    private int allergiesStatus;
    private int conditionStatus;
    private int weight;
    private int sleepTime;
    private int healthNotes;

}
