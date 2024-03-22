package org.api.controller;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.api.entity.FoodEntity;
import org.api.service.IngredientService;
import org.api.service.FoodRecordService;
import org.core.dto.MenuDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/menu_health_data")
@RequiredArgsConstructor
public class FoodRecordController {
    private final FoodRecordService foodRecordService;
    private final IngredientService ingredientService;

    @PostMapping("/menu")
    public ResponseEntity<?> postMenuData(@RequestBody @Valid MenuDto menuDto,
                                          @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        FoodEntity foodEntity = foodRecordService.saveMenuData(menuDto, authorizationHeader);
        ingredientService.saveIngredient(foodEntity, menuDto);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/menu")
    public ResponseEntity<Object> getMenuData(@RequestParam(name = "date") LocalDate date,
                                              @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        Optional<Object> menuResponse = foodRecordService.getMenuDataByDate(date, authorizationHeader);

        return menuResponse
                .map(data -> ResponseEntity.ok().body(data))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

}
