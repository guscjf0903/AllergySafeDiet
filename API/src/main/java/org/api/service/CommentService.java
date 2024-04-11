package org.api.service;

import lombok.RequiredArgsConstructor;
import org.api.entity.CommentEntity;
import org.api.entity.PostEntity;
import org.api.entity.UserEntity;
import org.api.repository.CommentRepository;
import org.api.service.post.PostService;
import org.core.request.CommentRequest;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;

    public void postComment(CommentRequest commentRequest, UserEntity user) {
        PostEntity postEntity = postService.getPostEntityFindById(commentRequest.postId());

        CommentEntity commentEntity = CommentEntity.of(postEntity, user, commentRequest.commentText());
        commentRepository.save(commentEntity);
    }

}
