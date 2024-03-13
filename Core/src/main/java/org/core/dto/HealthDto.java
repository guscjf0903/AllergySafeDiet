package org.core.dto;

import java.util.Date;

public record HealthDto (
        Date date,
        int allergiesStatus,
        int conditionStatus,
        int weight,
        int sleepTime,
        String healthNotes
){}


