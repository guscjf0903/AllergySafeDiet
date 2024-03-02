package org.core.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.valiator.Password;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignupDto {
    @NotBlank(message = "Please enter a username")
    private String userName;
    @Password
    @NotBlank(message = "Please enter a password")
    private String password;

    @NotBlank(message = "Please enter a email")
    @Email(message = "Please enter a valid email")
    private String email;
    private Date birthDate;
    private String gender;
    private int height;
    private boolean checkVerificationEmail;
}
