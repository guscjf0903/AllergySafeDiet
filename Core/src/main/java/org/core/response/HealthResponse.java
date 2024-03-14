package org.core.response;

import java.time.LocalDate;
import java.util.List;
import org.core.dto.PillsDto;


public record HealthResponse (
        LocalDate date,
        int allergiesStatus,
        int conditionStatus,
        int weight,
        int sleepTime,
        String healthNotes,
        List<PillsDto> pills
){
    public HealthResponse(LocalDate date, int allergiesStatus, int conditionStatus, int weight, int sleepTime, String healthNotes, List<PillsDto> pills) {
        this.date = date;
        this.allergiesStatus = allergiesStatus;
        this.conditionStatus = conditionStatus;
        this.weight = weight;
        this.sleepTime = sleepTime;
        this.healthNotes = healthNotes;
        this.pills = pills;
    }

    public static HealthResponse toResponse(LocalDate date, int allergiesStatus, int conditionStatus, int weight, int sleepTime, String healthNotes, List<PillsDto> pills) {
        return new HealthResponse(date, allergiesStatus, conditionStatus, weight, sleepTime, healthNotes, pills);
    }

}
