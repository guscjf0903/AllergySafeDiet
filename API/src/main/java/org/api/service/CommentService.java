package org.api.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.api.entity.CommentEntity;
import org.api.entity.PostEntity;
import org.api.entity.UserEntity;
import org.api.repository.CommentRepository;
import org.api.service.post.PostService;
import org.core.request.CommentRequest;
import org.core.response.CommentResponse;
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

    public List<CommentResponse> getCommentAndReplyByPostId(Long postId) {
        List<CommentEntity> commentEntities = commentRepository.findByPostPostId(postId);

        return createCommentResponse(commentEntities);
    }

    private List<CommentResponse> createCommentResponse(List<CommentEntity> commentEntities) {
        List<CommentResponse> commentResponseList = new ArrayList<>();
        for (CommentEntity commentEntity : commentEntities) {
            boolean isAuthor = commentEntity.getPost().getUser().getUsername()
                    .equals(commentEntity.getUser().getUsername());

            CommentResponse commentResponse = CommentResponse.toResponse(commentEntity.getCommentId(),
                    commentEntity.getUser().getUsername(), commentEntity.getContent(), isAuthor, null);

            commentResponseList.add(commentResponse);
        }

        return commentResponseList;
    }


}
