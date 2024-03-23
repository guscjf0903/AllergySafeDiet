package org.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.api.service.SignupService;
import org.core.dto.SignupRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SignupController {
    private final SignupService signupService;

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody @Valid SignupRequest signupRequest) {
        signupService.registerUser(signupRequest);
        return ResponseEntity.ok("User registered successfully");
    }
}

