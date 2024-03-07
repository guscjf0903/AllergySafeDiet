package org.core.dto;

import jakarta.validation.constraints.NotBlank;


public record LoginDto(
        @NotBlank(message = "Please enter a ID")
        String loginId,
        @NotBlank(message = "Please enter a password")
        String loginPassword
) {}