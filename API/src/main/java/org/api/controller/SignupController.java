package org.api.controller;

import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.api.service.UserService;
import org.core.request.SignupRequest;
import org.core.response.SignupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SignupController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> registerUser(@RequestBody @Valid SignupRequest signupRequest) {
        Optional<SignupResponse> signupResponse = userService.registerUser(signupRequest);

        return ResponseEntity.ok().body(signupResponse.get());
    }
}

