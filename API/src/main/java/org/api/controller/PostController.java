package org.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.api.entity.UserEntity;
import org.api.service.PostService;
import org.api.service.UserService;
import org.core.request.PostRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final UserService userService;

    @PostMapping("/upload")
    public ResponseEntity<Void> saveUploadDetail(@RequestBody @Valid PostRequest postRequest,
                                           Authentication authentication) {
        UserEntity user = userService.loadUserById((Long) authentication.getPrincipal());
        postService.saveUploadDetail(postRequest, user);

        return ResponseEntity.ok().build();
    }

}
