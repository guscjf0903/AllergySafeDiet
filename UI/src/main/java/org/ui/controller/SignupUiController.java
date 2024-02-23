package org.ui.controller;

import lombok.RequiredArgsConstructor;
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
        System.out.println("signupDTO = " + signupDTO.getUserName());
        System.out.println("signupDTO = " + signupDTO.getPassword());
        System.out.println("signupDTO = " + signupDTO.getGender());
        System.out.println("signupDTO = " + signupDTO.getBirthDate());
        System.out.println("signupDTO = " + signupDTO.getEmail());
        System.out.println("signupDTO = " + signupDTO.getHeight());

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> signupStatus = restTemplate.postForEntity(url + "/signup", signupDTO, String.class);

        return signupStatus;
    }
}
