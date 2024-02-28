package org.api.controller;

import lombok.RequiredArgsConstructor;
import org.api.service.SearchFoodRecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SearchFoodRecipeController {
    private final SearchFoodRecipeService searchFoodRecipeService;

    @GetMapping("/recipes")
    public String getFoodRecipes(@RequestParam(name = "foodName") String foodName) {
        System.out.println(foodName);
        searchFoodRecipeService.getFoodRecipes(foodName);
        return "foodName";
    }
}
