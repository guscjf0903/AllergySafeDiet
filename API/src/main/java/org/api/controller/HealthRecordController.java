package org.api.controller;


import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.api.entity.UserEntity;
import org.api.service.HealthRecordService;
import org.core.request.HealthRequest;
import org.core.response.HealthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/food_health_data")
@RequiredArgsConstructor
public class HealthRecordController {
    private final HealthRecordService healthRecordService;

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> getHealthData(@RequestParam(name = "date") LocalDate date,
                                                        @AuthenticationPrincipal UserEntity currentUser) {
        Optional<HealthResponse> healthResponse = healthRecordService.getHealthDataByDate(date, currentUser);
        return healthResponse
                .map(data -> ResponseEntity.ok().body(data))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @PostMapping("/health")
    public ResponseEntity<Void> postHealthData(@RequestBody HealthRequest healthRequest,
                                               @AuthenticationPrincipal UserEntity currentUser) {
        healthRecordService.saveHealthData(healthRequest, currentUser);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/health")
    public ResponseEntity<Void> putHealthData(@RequestBody HealthRequest healthRequest,
                                              @AuthenticationPrincipal UserEntity currentUser) {
        healthRecordService.putHealthData(healthRequest, currentUser);
        return ResponseEntity.ok().build();
    }

}
