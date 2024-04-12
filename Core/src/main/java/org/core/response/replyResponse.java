package org.core.response;

public record replyResponse(
   String replyText,
   String replyAuthor,
   boolean isAuthor
) {}
