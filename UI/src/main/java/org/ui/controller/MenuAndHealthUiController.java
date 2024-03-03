package org.ui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/menu_health_data")
public class MenuAndHealthUiController {
    @Value("${api.url}") String apiUrl;

//    @GetMapping("/calendar")
//    public String showDataCalender() {
//        return "DataCalender";
//    }

    @GetMapping("menu/{date}")
    public String showMenuDataForm(@PathVariable("date") String date, Model model) {
        model.addAttribute("date", date);
        model.addAttribute("apiUrl", apiUrl);
        return "FoodMenuInfo";
    }

    @GetMapping("health/{date}")
    public String showHealthDataForm(@PathVariable("date") String date, Model model) {
        model.addAttribute("date", date);
        model.addAttribute("apiUrl", apiUrl);
        return "HealthInfo";
    }

}
