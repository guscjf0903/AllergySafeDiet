package org.ui.controller;

import lombok.RequiredArgsConstructor;
import org.core.dto.LoginDto;
import org.core.response.LoginResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

@Controller
@RequiredArgsConstructor
public class LoginUiController {
    @GetMapping("/login")
    public String showLoginForm() {
        return "LoginAndSingupForm";
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginDto loginDto, @Value("${api.url}") String url) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<LoginResponse> loginStatus = restTemplate.postForEntity(url + "/login", loginDto, LoginResponse.class);

        return loginStatus;
    }
}
