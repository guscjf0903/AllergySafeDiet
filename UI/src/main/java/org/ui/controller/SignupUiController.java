package org.ui.controller;

import lombok.RequiredArgsConstructor;
import org.core.dto.EmailDto;
import org.core.dto.SignupDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

@Controller
@RequiredArgsConstructor
public class SignupUiController {
    @GetMapping("/signup")
    public String showSignUpForm() {
        return "LoginAndSignupForm";
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignupDto signupDTO, @Value("${api.url}") String url) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> signupStatus = restTemplate.postForEntity(url + "/signup", signupDTO, String.class);

        return signupStatus;
    }

    @PostMapping("/email/verification_request")
    public ResponseEntity<String> sendVerificationEmail(@RequestBody EmailDto emailDto, @Value("${api.url}") String url) {
        System.out.println(emailDto.getEmail());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> verificationStatus = restTemplate.postForEntity(url + "/email/verification_request", emailDto, String.class);

        return verificationStatus;
    }
}
