package org.core.request;


import jakarta.validation.constraints.Email;

public record EmailRequest(
        @Email
        String email
) {}
