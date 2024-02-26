package org.ui.controller;

import jakarta.websocket.server.PathParam;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.client.RestTemplate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/menu-and-health-data")
public class MenuAndHealthUiController {

    @GetMapping("/calendar")
    public String showDataCalender() {
        return "DataCalender";
    }

    @GetMapping("/edit/{date}")
    public String showNewMenuAndHealthDataForm(@PathVariable("date") String date, Model model) {
        model.addAttribute("date", date);
        return "NewMenuAndHealthForm";
    }

    @PostMapping("/menu")
    public ResponseEntity postMenuData(@RequestBody MenuDto menuDto, @Value("${api.url}") String url) {
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.postForEntity(url + "/menu-and-health-data/menu", menuDto, ResponseEntity.class);
    }
}
