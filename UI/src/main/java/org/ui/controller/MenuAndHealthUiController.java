package org.ui.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/menu-and-health-data")
public class MenuAndHealthUiController {

    @GetMapping("/data-calender")
    public String showDataCalender() {
        return "DataCalender";
    }

    @GetMapping("/new-data/{date}")
    public String showNewMenuAndHealthDataForm(@PathVariable("date") String date, Model model) {
        model.addAttribute("date", date);
        return "NewMenuAndHealthForm";
    }

    @GetMapping("/menu_suggestions")
    public ResponseEntity<List> getMenuSuggestions(@RequestParam String menuName) {
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForEntity("http://localhost:8080/menu-and-health-data/menu-suggestions?menuName=" + menuName, List.class);
    }


}
