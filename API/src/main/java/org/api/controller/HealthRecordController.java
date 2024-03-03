package org.api.controller;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.api.entity.FoodEntity;
import org.api.entity.HealthEntity;
import org.api.service.FoodRecordService;
import org.api.service.HealthRecordService;
import org.api.service.IngredientService;
import org.core.dto.HealthDto;
import org.core.dto.MenuDto;
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
    public ResponseEntity<?> getHealthData(@RequestParam(name = "date") LocalDate date) {
        Optional<HealthDto> healthDto = healthRecordService.getHealthDataByDate(date);

        return healthDto
                .map(data -> ResponseEntity.ok().body(data))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

}
