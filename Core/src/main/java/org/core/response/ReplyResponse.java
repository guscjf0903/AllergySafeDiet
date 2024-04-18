package org.core.response;

public record ReplyResponse(
   Long id,
   String replyText,
   String replyAuthor,
   boolean isPostReplyAuthor,
   boolean isOwnReply

) {
    public static ReplyResponse toResponse(Long id,String replyText, String replyAuthor, boolean isPostReplyAuthor, boolean isOwnReply) {
        return new ReplyResponse(id, replyText, replyAuthor, isPostReplyAuthor, isOwnReply);


    }
}
