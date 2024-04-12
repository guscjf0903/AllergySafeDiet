package org.api.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.api.entity.CommentEntity;
import org.api.entity.PostEntity;
import org.api.entity.ReplyEntity;
import org.api.entity.UserEntity;
import org.api.repository.CommentRepository;
import org.api.repository.ReplyRepository;
import org.api.service.post.PostService;
import org.core.request.CommentRequest;
import org.core.request.ReplyRequest;
import org.core.response.CommentResponse;
import org.core.response.ReplyResponse;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
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
            List<ReplyResponse> replyResponseList = new ArrayList<>();

            List<ReplyEntity> replyEntities = replyRepository.findByCommentEntityCommentId(
                    commentEntity.getCommentId());

            for (ReplyEntity replyEntity : replyEntities) {
                boolean isReplyAuthor = commentEntity.getPost().getUser().getUsername()
                        .equals(replyEntity.getUser().getUsername());
                ReplyResponse replyResponse = ReplyResponse.toResponse(replyEntity.getReplyText(),
                        replyEntity.getUser().getUsername(), isReplyAuthor);

                replyResponseList.add(replyResponse);
            }

            boolean isCommentAuthor = commentEntity.getPost().getUser().getUsername()
                    .equals(commentEntity.getUser().getUsername());

            CommentResponse commentResponse = CommentResponse.toResponse(commentEntity.getCommentId(),
                    commentEntity.getUser().getUsername(), commentEntity.getContent(), isCommentAuthor,
                    replyResponseList);

            commentResponseList.add(commentResponse);
        }

        return commentResponseList;
    }

    public void postReply(ReplyRequest replyRequest, UserEntity user) {
        CommentEntity commentEntity = commentRepository.findByCommentId(replyRequest.commentId());
        ReplyEntity replyEntity = ReplyEntity.of(commentEntity, user, replyRequest.replyText());

        replyRepository.save(replyEntity);
    }

}
