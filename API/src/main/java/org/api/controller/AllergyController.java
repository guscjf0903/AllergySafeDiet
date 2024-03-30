package org.api.controller;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.api.entity.UserEntity;
import org.api.service.AllergyService;
import org.api.service.UserService;
import org.core.request.AllergyRequest;
import org.core.response.AllergyResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AllergyController {
    private final AllergyService allergyService;
    private final UserService userService;

    @GetMapping("/allergy")
    public ResponseEntity<AllergyResponse> getAllergyData(
            Authentication authentication) {
        UserEntity user = userService.loadUserById((Long) authentication.getPrincipal());

        Optional<AllergyResponse> allergyDataOptional = allergyService.getAllergyData(user);

        return allergyDataOptional.map(data -> ResponseEntity.ok().body(data))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/allergy")
    public ResponseEntity<Void> postAllergyData(@RequestBody AllergyRequest allergyRequest,
                                                Authentication authentication) {
        UserEntity user = userService.loadUserById((Long) authentication.getPrincipal());

        allergyService.saveAllergyData(allergyRequest, user);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/allergy")
    public ResponseEntity<Void> putAllergyData(@RequestBody AllergyRequest allergyRequest,
                                               Authentication authentication) {
        UserEntity user = userService.loadUserById((Long) authentication.getPrincipal());

        allergyService.putAllergyData(allergyRequest, user);

        return ResponseEntity.ok().build();
    }
}
