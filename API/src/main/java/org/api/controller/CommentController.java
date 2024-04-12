package org.api.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.api.entity.UserEntity;
import org.api.service.CommentService;
import org.api.service.UserService;
import org.core.request.CommentRequest;
import org.core.response.CommentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final UserService userService;
    private final CommentService commentService;
    @PostMapping("/create")
    public ResponseEntity<Void> postComment(@RequestBody CommentRequest commentRequest,
                                              Authentication authentication) {
        UserEntity user = userService.loadUserById((Long) authentication.getPrincipal());
        commentService.postComment(commentRequest, user);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getCommentsByPostId(@RequestParam("postId") Long postId) {
        List<CommentResponse> comments = commentService.getCommentAndReplyByPostId(postId);

        return ResponseEntity.ok(comments);
    }

}