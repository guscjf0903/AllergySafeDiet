package org.core.response;

import java.time.LocalDate;
import java.util.List;
import org.core.dto.PillsRequest;


public record HealthResponse (
        LocalDate date,
        int allergiesStatus,
        int conditionStatus,
        int weight,
        int sleepTime,
        String healthNotes,
        List<PillsRequest> pills
){
    public static HealthResponse toResponse(LocalDate date, int allergiesStatus, int conditionStatus, int weight, int sleepTime, String healthNotes, List<PillsRequest> pills) {
        return new HealthResponse(date, allergiesStatus, conditionStatus, weight, sleepTime, healthNotes, pills);
    }
}
