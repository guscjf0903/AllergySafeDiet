package org.core.dto;

import java.time.LocalDate;

public record HealthDto (
        LocalDate date,
        int allergiesStatus,
        int conditionStatus,
        int weight,
        int sleepTime,
        String healthNotes
){}


