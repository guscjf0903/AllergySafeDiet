package org.core.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Date;
import org.core.valiator.Password;


public record SignupRequest(
        @NotBlank(message = "Please enter a username")
        String userName,
        @Password
        @NotBlank(message = "Please enter a password")
        String password,
        Date birthDate,
        String gender,
        int height
) {}
