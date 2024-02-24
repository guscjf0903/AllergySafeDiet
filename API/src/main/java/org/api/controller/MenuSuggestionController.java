package org.api.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/menu-and-health-data")
@RequiredArgsConstructor
public class MenuSuggestionController {
    private final MenuSuggestionService menuSuggestionService;

    @GetMapping("/menu-suggestions")
    public List<String> getMenuSuggestions(@RequestParam String menuName) {
        return menuSuggestionService.getMenuSuggestions(menuName);
    }

}
