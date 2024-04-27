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

    public List<CommentResponse> getCommentAndReplyByPostId(Long postId, UserEntity user) {
        List<CommentEntity> commentEntities = commentRepository.findByPostPostId(postId);

        return createCommentResponse(commentEntities, user);
    }

    private List<CommentResponse> createCommentResponse(List<CommentEntity> commentEntities, UserEntity user) {
        List<CommentResponse> commentResponseList = new ArrayList<>();

        for (CommentEntity commentEntity : commentEntities) {
            List<ReplyResponse> replyResponseList = new ArrayList<>();

            List<ReplyEntity> replyEntities = replyRepository.findByCommentEntityCommentId(
                    commentEntity.getCommentId());

            for (ReplyEntity replyEntity : replyEntities) {
                boolean isReplyPostAuthor = commentEntity.getPost().getUser().getUserId()
                        .equals(replyEntity.getUser().getUserId()); //대댓글이 게시글 작성자와 동일한지 확인

                boolean isOwnReply = replyEntity.getUser().getUserId()
                        .equals(user.getUserId()); //해당 게시글을 보는 사용자가 대댓글 작성자인지 확인

                ReplyResponse replyResponse = ReplyResponse.toResponse(replyEntity.getReplyId() ,replyEntity.getReplyText(),
                        replyEntity.getUser().getUsername(), isReplyPostAuthor, isOwnReply);
                replyResponseList.add(replyResponse);
            }

            boolean isCommentPostAuthor = commentEntity.getPost().getUser().getUserId()
                    .equals(commentEntity.getUser().getUserId()); // 댓글이 게시글 작성자와 동일한지 확인
            boolean isOwnComment = commentEntity.getUser().getUserId()
                    .equals(user.getUserId()); //해당 게시글을 보는 사용자가 댓글 작성자인지 확인

            CommentResponse commentResponse = CommentResponse.toResponse(commentEntity.getCommentId(),
                    commentEntity.getUser().getUsername(), commentEntity.getContent(), isCommentPostAuthor,
                    isOwnComment, replyResponseList);

            commentResponseList.add(commentResponse);
        }

        return commentResponseList;
    }

    public void postReply(ReplyRequest replyRequest, UserEntity user) {
        CommentEntity commentEntity = commentRepository.findByCommentId(replyRequest.commentId());
        ReplyEntity replyEntity = ReplyEntity.of(commentEntity, user, replyRequest.replyText());

        replyRepository.save(replyEntity);
    }

    public void deleteComment(long commentId) {
        commentRepository.deleteById(commentId);
    }

    public void deleteReply(long replyId) {
        replyRepository.deleteById(replyId);
    }

}
