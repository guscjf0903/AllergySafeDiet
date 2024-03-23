package org.core.request;

import java.time.LocalDate;
import java.util.List;

public record HealthRequest(
        LocalDate date,
        int allergiesStatus,
        int conditionStatus,
        int weight,
        int sleepTime,
        String healthNotes,
        List<PillsRequest> pills
){}


