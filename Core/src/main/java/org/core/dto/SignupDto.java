package org.core.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Date;
import org.core.valiator.Password;


public record SignupDto(
        @NotBlank(message = "Please enter a username")
        String userName,
        @Password
        @NotBlank(message = "Please enter a password")
        String password,
        @NotBlank(message = "Please enter a email")
        @Email(message = "Please enter a valid email")
        String email,
        Date birthDate,
        String gender,
        int height,
        boolean checkVerificationEmail
) {}
