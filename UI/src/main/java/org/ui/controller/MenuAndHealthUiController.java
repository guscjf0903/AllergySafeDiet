package org.ui.controller;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
@RequestMapping("/menu_health_data")
public class MenuAndHealthUiController {
    @Value("${api.url}") String apiUrl;

    @GetMapping("/select_date")
    public String showDataCalender(Model model) {
        model.addAttribute("apiUrl", apiUrl);
        return "SelectMenuAndHealthDate";
    }

    @GetMapping("/menu")
    public String showMenuDataForm(@RequestParam(name = "date") LocalDate date, Model model) {
        model.addAttribute("date", date);
        model.addAttribute("apiUrl", apiUrl);
        return "FoodMenuInfo";
    }

    @GetMapping("/menu/edit")
    public String showMenuDataEditForm(@RequestParam(name = "id") Long id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("apiUrl", apiUrl);
        return "FoodMenuEditInfo";
    }

    @GetMapping("/health")
    public String showHealthDataForm(@RequestParam(name = "date") LocalDate date, Model model) {
        model.addAttribute("date", date);
        model.addAttribute("apiUrl", apiUrl);
        return "HealthInfo";
    }

}
