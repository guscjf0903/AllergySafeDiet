package org.api.controller;


import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.api.service.HealthRecordService;
import org.core.dto.HealthDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/menu_health_data")
@RequiredArgsConstructor
public class HealthRecordController {
    private final HealthRecordService healthRecordService;

    @GetMapping("/health")
    public ResponseEntity<?> getHealthData(@RequestParam(name = "date") LocalDate date, @RequestParam(name = "loginToken") String loginToken) {
        Optional<HealthDto> healthDto = healthRecordService.getHealthDataByDate(date, loginToken);

        return healthDto
                .map(data -> ResponseEntity.ok().body(data))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @PostMapping("/health")
    public ResponseEntity<?> postHealthData(@RequestBody HealthDto healthDto) {
        healthRecordService.saveHealthData(healthDto);
        return ResponseEntity.ok().build();
    }

}
