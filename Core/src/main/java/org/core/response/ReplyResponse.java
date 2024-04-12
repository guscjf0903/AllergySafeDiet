package org.core.response;

import java.util.List;

public record ReplyResponse(
   String replyText,
   String replyAuthor,
   boolean isAuthor
) {
    public static ReplyResponse toResponse(String replyText, String replyAuthor, boolean isAuthor) {
        return new ReplyResponse(replyText, replyAuthor, isAuthor);
    }
}
