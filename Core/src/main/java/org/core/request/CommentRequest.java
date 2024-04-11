package org.core.request;

import jakarta.validation.constraints.NotBlank;

public record CommentRequest(
        @NotBlank
        Long postId,
        @NotBlank
        String commentText
)
{ }
