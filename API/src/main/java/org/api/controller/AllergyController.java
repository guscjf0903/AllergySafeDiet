package org.api.controller;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.api.entity.UserEntity;
import org.api.service.AllergyService;
import org.core.request.AllergyRequest;
import org.core.response.AllergyResponse;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/allergy")
    public ResponseEntity<AllergyResponse> getAllergyData(
            @AuthenticationPrincipal UserEntity currentUser) {
        Optional<AllergyResponse> allergyDataOptional = allergyService.getAllergyData(currentUser);

        return allergyDataOptional.map(data -> ResponseEntity.ok().body(data))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/allergy")
    public ResponseEntity<Void> postAllergyData(@RequestBody AllergyRequest allergyRequest,
                                                @AuthenticationPrincipal UserEntity currentUser) {
        allergyService.saveAllergyData(allergyRequest, currentUser);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/allergy")
    public ResponseEntity<Void> putAllergyData(@RequestBody AllergyRequest allergyRequest,
                                               @AuthenticationPrincipal UserEntity currentUser) {
        allergyService.putAllergyData(allergyRequest, currentUser);

        return ResponseEntity.ok().build();
    }
}
