package org.core.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record PostListResponse (
        List<PostSummary> posts, // 포스트 리스트
        int currentPage,      // 현재 페이지 번호
        int totalItems,      // 전체 아이템 수
        int totalPages       // 전체 페이지 수
){
    public static PostListResponse toResponse(List<PostSummary> posts, int currentPage, int totalItems, int totalPages) {
        return new PostListResponse(posts, currentPage,totalItems,totalPages);
    }
}
