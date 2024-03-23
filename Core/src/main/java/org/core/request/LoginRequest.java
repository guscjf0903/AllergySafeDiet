package org.core.request;

import jakarta.validation.constraints.NotBlank;


public record LoginRequest(
        @NotBlank(message = "Please enter a ID")
        String loginId,
        @NotBlank(message = "Please enter a password")
        String loginPassword
) {}