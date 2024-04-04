package org.core.response;

import java.time.LocalDate;
import java.util.List;
import org.core.request.PillsRequest;


public record HealthResponse (
        Long id,
        LocalDate date,
        int allergiesStatus,
        int conditionStatus,
        int weight,
        int sleepTime,
        String healthNotes,
        List<PillsRequest> pills
){
    public static HealthResponse toResponse(Long id, LocalDate date, int allergiesStatus, int conditionStatus, int weight, int sleepTime, String healthNotes, List<PillsRequest> pills) {
        return new HealthResponse(id, date, allergiesStatus, conditionStatus, weight, sleepTime, healthNotes, pills);
    }
}
