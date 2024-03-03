package org.ui.controller;

import lombok.RequiredArgsConstructor;
import org.core.dto.EmailDto;
import org.core.dto.SignupDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

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
