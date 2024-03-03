package org.ui.controller;

import jakarta.websocket.server.PathParam;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.core.dto.HealthDto;
import org.core.dto.IngredientsDto;
import org.core.dto.MenuDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/menu_health_data")
public class MenuAndHealthUiController {
    @Value("${api.url}") String apiUrl;

    @GetMapping("/calendar")
    public String showDataCalender() {
        return "DataCalender";
    }

    @GetMapping("menu/{date}")
    public String showMenuDataForm(@PathVariable("date") String date, Model model) {
        model.addAttribute("date", date);
        model.addAttribute("apiUrl", apiUrl);

        return "FoodMenuInfo";
    }



    @GetMapping("health/{date}")
    public String showHealthDataForm(@PathVariable("date") String date, Model model) {
        model.addAttribute("date", date);
        return "HealthInfo";
    }
    @GetMapping("/health")
    public ResponseEntity<HealthDto> checkHealthData(@RequestParam(name = "date") LocalDate date, @Value("${api.url}") String url) {
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForEntity(url + "/menu_health_data/health?date=" + date, HealthDto.class);
    }
    @PostMapping("/health")
    public ResponseEntity<?> postHealthData(@RequestBody HealthDto healthDto, @Value("${api.url}") String url) {
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.postForEntity(url + "/menu_health_data/health", healthDto, ResponseEntity.class);
    }

}
