package org.core.dto;


import jakarta.validation.constraints.Email;

public record EmailRequest(
        @Email
        String email
) {}
