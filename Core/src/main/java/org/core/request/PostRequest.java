package org.core.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record PostRequest(
        @NotNull
        String title,
        @NotNull
        String content,
        List<Long> foodIds,
        List<Long>healthId
){}
