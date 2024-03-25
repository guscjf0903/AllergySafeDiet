package org.ui.controller;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class SignupUiController {
    @Value("${api.url}") String apiUrl;

    @GetMapping("/signup")
    public String showSignUpForm(Model model) {
        model.addAttribute("apiUrl", apiUrl);
        return "LoginAndSignupForm";
    }

    @GetMapping("/verify_email")
    public String showVerifyEmailForm(@RequestParam(name = "userPk") Long userPk, Model model) {
        model.addAttribute("userPk", userPk);
        model.addAttribute("apiUrl", apiUrl);
        return "VerifyEmailForm";
    }
}
