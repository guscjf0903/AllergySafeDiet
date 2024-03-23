package org.core.request;

public record PillsRequest(
        String name,
        int count
){}