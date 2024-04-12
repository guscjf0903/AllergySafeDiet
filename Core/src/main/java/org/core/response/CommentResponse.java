package org.core.response;


import java.util.List;

public record CommentResponse(
        Long id,
        String author,
        String text,
        boolean isAuthor,
        List<ReplyResponse> replies
) {
    public static CommentResponse toResponse(long id, String author, String text, boolean isAuthor, List<ReplyResponse> replies) {
        return new CommentResponse(id, author, text, isAuthor, replies);
    }
}
