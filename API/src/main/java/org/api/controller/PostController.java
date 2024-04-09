package org.api.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.api.entity.UserEntity;
import org.api.service.FileUploadService;
import org.api.service.PostService;
import org.api.service.UserService;
import org.core.request.PostRequest;
import org.core.response.PostDetailResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final FileUploadService fileUploadService;

    @PostMapping("/upload")
    public ResponseEntity<Void> saveUploadDetail(@RequestParam("title") String title,
                                                 @RequestParam("content") String content,
                                                 @RequestParam(value = "foodIds", required = false) List<Long> foodIds,
                                                 @RequestParam(value = "healthIds", required = false) List<Long> healthIds,
                                                 @RequestParam(value = "images", required = false) List<MultipartFile> images,
                                                 Authentication authentication) {
        PostRequest postRequest = new PostRequest(title, content, foodIds, healthIds, images);

        UserEntity user = userService.loadUserById((Long) authentication.getPrincipal());
        List<String> fileUrls = fileUploadService.uploadFiles(postRequest.images());
        postService.saveUploadDetail(postRequest, user, fileUrls);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/detail")
    public ResponseEntity<PostDetailResponse> getPostDetailData(@RequestParam("postId") Long postId) {
        PostDetailResponse postDetailResponse = postService.getPostDetail(postId);

        return ResponseEntity.ok().body(postDetailResponse);
    }


}
