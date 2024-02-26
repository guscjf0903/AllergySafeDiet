package org.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.api.entity.FoodEntity;
import org.api.entity.IngredientEntity;
import org.api.service.IngredientService;
import org.api.service.FoodRecordService;
import org.core.dto.MenuDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/menu-and-health-data")
@RequiredArgsConstructor
public class FoodRecordController {
    private final FoodRecordService foodRecordService;
    private final IngredientService ingredientService;

    @PostMapping("/menu")
    public ResponseEntity postMenuData(@RequestBody @Valid MenuDto menuDto) {
        FoodEntity foodEntity = foodRecordService.saveMenuData(menuDto);
        ingredientService.saveIngredient(foodEntity, menuDto);

        return ResponseEntity.ok().build();
    }

}
