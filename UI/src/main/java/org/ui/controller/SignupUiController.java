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

    @PostMapping("/email/verification_request")
    public ResponseEntity<String> sendVerificationEmail(@RequestBody EmailDto emailDto, @Value("${api.url}") String url) {
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.postForEntity(url + "/email/verification_request", emailDto, String.class);
    }

    @GetMapping("/emails/verifications")
    public ResponseEntity<String> verificationEmail(@RequestParam String email,@RequestParam int verificationCode, @Value("${api.url}") String url) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(url + "/emails/verifications?email=" + email + "&code=" + verificationCode, String.class);
    }
}
