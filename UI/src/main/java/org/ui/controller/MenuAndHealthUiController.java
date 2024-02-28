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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/menu_health_data")
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
    public ResponseEntity<?> postMenuData(@RequestBody MenuDto menuDto, @Value("${api.url}") String url) {
        RestTemplate restTemplate = new RestTemplate();
        try{
            return restTemplate.postForEntity(url + "/menu_health_data/menu", menuDto, ResponseEntity.class);
        }catch (HttpClientErrorException | HttpServerErrorException e ) { //api에서 보낸 에러메세지를 받아오기 위해
            System.out.println(e.getResponseBodyAsString());
            System.out.println(e.getStatusCode());
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    @GetMapping("/recipes")
    public ResponseEntity<?> getFoodRecipes(@RequestParam(name = "foodName") String foodName, @Value("${api.url}") String url) {
        System.out.println(foodName);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(url + "/recipes?foodName=" + foodName, ResponseEntity.class);
    }
}
