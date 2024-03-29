package org.api.controller;

import jakarta.validation.Valid;
import java.nio.file.attribute.UserPrincipal;
import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.api.entity.FoodEntity;
import org.api.entity.UserEntity;
import org.api.service.IngredientService;
import org.api.service.FoodRecordService;
import org.core.request.FoodRequest;
import org.core.response.FoodResponse;
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
public class FoodRecordController {
    private final FoodRecordService foodRecordService;
    private final IngredientService ingredientService;

    @PostMapping("/food")
    public ResponseEntity<?> saveFoodAndIngredientData(@RequestBody @Valid FoodRequest foodRequest,
                                                       @AuthenticationPrincipal UserEntity currentUser) {
        FoodEntity foodEntity = foodRecordService.saveFoodData(foodRequest, currentUser);
        ingredientService.saveIngredientData(foodEntity, foodRequest);

        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/food", params = "date")
    public ResponseEntity<Object> getFoodDataByDate(@RequestParam(name = "date") LocalDate date,
                                                    @AuthenticationPrincipal UserEntity currentUser) {
        Optional<Object> menuResponse = foodRecordService.getFoodDataByDate(date, currentUser);

        return menuResponse
                .map(data -> ResponseEntity.ok().body(data))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @GetMapping(value = "/food", params = "id")
    public ResponseEntity<FoodResponse> getFoodDataById(@RequestParam(name = "id") Long id,
                                                        @AuthenticationPrincipal UserEntity currentUser) {
        Optional<FoodResponse> menuResponse = foodRecordService.getFoodDataById(id, currentUser);

        return menuResponse
                .map(data -> ResponseEntity.ok().body(data))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @PutMapping(value = "/food")
    public ResponseEntity<FoodResponse> putFoodDataById(@RequestParam(name = "id") Long id,
                                                        @RequestBody FoodRequest foodRequest,
                                                        @AuthenticationPrincipal UserEntity currentUser) {
        foodRecordService.putFoodData(id, foodRequest, currentUser);
        return ResponseEntity.ok().build();
    }

}
