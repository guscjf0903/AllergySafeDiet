package org.core.response;

import java.time.LocalDateTime;

public record PostSummary (
        long postId,
        LocalDateTime createdAt,
        String title,
        String author,
        long views
) {}
