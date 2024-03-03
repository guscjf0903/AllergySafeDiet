package org.ui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class SignupUiController {
    @Value("${api.url}") String apiUrl;

    @GetMapping("/signup")
    public String showSignUpForm(Model model) {
        model.addAttribute("apiUrl", apiUrl);
        return "LoginAndSignupForm";
    }
}
