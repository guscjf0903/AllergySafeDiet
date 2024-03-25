package org.ui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AllergyInfoUiController {
    @Value("${api.url}") String apiUrl;
    @GetMapping("/allergy_info")
    public String showSignUpForm(Model model) {
        model.addAttribute("apiUrl", apiUrl);
        return "AllergyInfoForm";
    }


}
