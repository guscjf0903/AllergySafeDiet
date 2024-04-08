package org.ui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostUiController {
    @Value("${api.url}") String apiUrl;
    @GetMapping("/list")
    public String showPostListForm(Model model) {
        model.addAttribute("apiUrl", apiUrl);
        return "PostListForm";
    }

    @GetMapping("/upload")
    public String showPostUploadForm(Model model) {
        model.addAttribute("apiUrl", apiUrl);
        return "PostUploadForm";
    }

    @GetMapping("/detail")
    public String showPostDetailForm(Model model,@RequestParam(name = "post_id") Long post_id) {
        model.addAttribute("apiUrl", apiUrl);
        model.addAttribute("post_id", post_id);

        return "PostDetailForm";
    }

}