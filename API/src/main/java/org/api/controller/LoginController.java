package org.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.api.service.LoginService;
import org.core.request.LoginRequest;
import org.core.response.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse loginResponse = loginService.loginUser(loginRequest);

        return ResponseEntity.ok(loginResponse);
    }
}
