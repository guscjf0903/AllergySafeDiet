package org.api.controller;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.api.service.SearchFoodRecipeService;
import org.core.request.IngredientsRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SearchFoodRecipeController {
    private final SearchFoodRecipeService searchFoodRecipeService;

    @GetMapping("/recipes")
    public ResponseEntity<List<IngredientsRequest>> getFoodRecipes(@RequestParam(name = "foodName") String foodName) {
        List<String> searchFoodIngredient = searchFoodRecipeService.getFoodRecipes(foodName);

        return ResponseEntity.ok(searchFoodIngredient.stream()
                .map(IngredientsRequest::new)
                .collect(Collectors.toList()));
    }
}
