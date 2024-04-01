package org.api.controller;


import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.api.entity.UserEntity;
import org.api.service.HealthRecordService;
import org.api.service.UserService;
import org.core.request.HealthRequest;
import org.core.response.HealthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/food_health_data")
@RequiredArgsConstructor
public class HealthRecordController {
    private final HealthRecordService healthRecordService;
    private final UserService userService;

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> getHealthData(@RequestParam(name = "date") LocalDate date,
                                                        Authentication authentication) {
        UserEntity user = userService.loadUserById((Long) authentication.getPrincipal());

        Optional<HealthResponse> healthResponse = healthRecordService.getHealthDataByDate(date, user);
        return healthResponse
                .map(data -> ResponseEntity.ok().body(data))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @PostMapping("/health")
    public ResponseEntity<Void> postHealthData(@RequestBody HealthRequest healthRequest,
                                               Authentication authentication) {
        UserEntity user = userService.loadUserById((Long) authentication.getPrincipal());

        healthRecordService.saveHealthData(healthRequest, user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/health")
    public ResponseEntity<Void> putHealthData(@RequestBody HealthRequest healthRequest,
                                              Authentication authentication) {
        UserEntity user = userService.loadUserById((Long) authentication.getPrincipal());

        healthRecordService.putHealthData(healthRequest, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/health")
    public ResponseEntity<Void> deleteHealthData(@RequestParam(name = "date") LocalDate date,
                                                 Authentication authentication) {
        UserEntity user = userService.loadUserById((Long) authentication.getPrincipal());
        healthRecordService.deleteHealthDataByDate(date, user);

        return ResponseEntity.ok().build();
    }

}
