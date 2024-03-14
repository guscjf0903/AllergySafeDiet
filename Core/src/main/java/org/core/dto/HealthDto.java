package org.core.dto;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public record HealthDto (
        LocalDate date,
        int allergiesStatus,
        int conditionStatus,
        int weight,
        int sleepTime,
        String healthNotes,
        List<PillsDto> pills
){}


