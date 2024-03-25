package org.api.controller;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.api.service.AllergyService;
import org.core.request.AllergyRequest;
import org.core.response.AllergyResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AllergyController {
    private final AllergyService allergyService;

    @GetMapping("/allergy")
    public ResponseEntity<AllergyResponse> getAllergyData(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        Optional<AllergyResponse> allergyDataOptional = allergyService.getAllergyData(authorizationHeader);

        return allergyDataOptional.map(data -> ResponseEntity.ok().body(data))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/allergy")
    public ResponseEntity<Void> postAllergyData(@RequestBody AllergyRequest allergyRequest,
                                             @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        allergyService.saveAllergyData(allergyRequest, authorizationHeader);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/allergy")
    public ResponseEntity<Void> putAllergyData(@RequestBody AllergyRequest allergyRequest,
                                                @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        allergyService.putAllergyData(allergyRequest, authorizationHeader);

        return ResponseEntity.ok().build();
    }}
