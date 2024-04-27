package org.api.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.api.entity.UserEntity;
import org.api.service.CommentService;
import org.api.service.UserService;
import org.core.request.CommentRequest;
import org.core.request.ReplyRequest;
import org.core.response.CommentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<List<CommentResponse>> getCommentsByPostId(@RequestParam("postId") Long postId, Authentication authentication) {
        UserEntity user = userService.loadUserById((Long) authentication.getPrincipal());

        List<CommentResponse> comments = commentService.getCommentAndReplyByPostId(postId, user);

        return ResponseEntity.ok(comments);
    }

    @PostMapping("/reply")
    public ResponseEntity<Void> postReply(@RequestBody ReplyRequest replyRequest,
                                          Authentication authentication) {
        UserEntity user = userService.loadUserById((Long) authentication.getPrincipal());
        commentService.postReply(replyRequest, user);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/reply/{replyId}")
    public ResponseEntity<Void> deleteReply(@PathVariable long replyId) {
        commentService.deleteReply(replyId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable long commentId) {
        commentService.deleteComment(commentId);

        return ResponseEntity.ok().build();
    }

}
