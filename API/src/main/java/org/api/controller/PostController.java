package org.api.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.api.entity.UserEntity;
import org.api.service.FileUploadService;
import org.api.service.PostService;
import org.api.service.UserService;
import org.core.request.PostRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final FileUploadService fileUploadService;

    @PostMapping("/upload")
    public ResponseEntity<Void> saveUploadDetail(@ModelAttribute PostRequest postRequest,
                                           Authentication authentication) {
        UserEntity user = userService.loadUserById((Long) authentication.getPrincipal());
        List<String> fileUrls = fileUploadService.uploadFiles(postRequest.images());
        postService.saveUploadDetail(postRequest, user, fileUrls);

        return ResponseEntity.ok().build();
    }

}
