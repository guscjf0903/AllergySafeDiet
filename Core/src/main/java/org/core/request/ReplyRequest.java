package org.core.request;

import jakarta.validation.constraints.NotBlank;

public record ReplyRequest(
        @NotBlank
        Long commentId,
        @NotBlank
        String replyText
)
{ }
