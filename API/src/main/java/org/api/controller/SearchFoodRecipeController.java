package org.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.api.service.SearchFoodRecipeService;
import org.core.dto.IngredientsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SearchFoodRecipeController {
    private final SearchFoodRecipeService searchFoodRecipeService;

    @GetMapping("/recipes")
    public ResponseEntity<List<IngredientsDto>> getFoodRecipes(@RequestParam(name = "foodName") String foodName) throws IOException {
        List<IngredientsDto> searchFoodIngredient = searchFoodRecipeService.getFoodRecipes(foodName);
        return ResponseEntity.ok(searchFoodIngredient);
    }
}