package org.core.response;

public record PostSummary (
        long postId,
        String title,
        String author,
        long views
) {}
