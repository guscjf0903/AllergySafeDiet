package org.ui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/main_menu")
public class MainMenuUiController {
    @Value("${api.url}") String apiUrl;
    @Value("${ui.url}") String uiUrl;


    @GetMapping("/select")
    public String showDataCalender(Model model) {
        model.addAttribute("uiUrl", uiUrl);
        return "SelectMainMenu";
    }
}
