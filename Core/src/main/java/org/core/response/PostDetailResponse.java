package org.core.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record PostDetailResponse(
        String title,
        String content,
        String author,
        int views,
        LocalDateTime date,
        List<HealthResponse> healthDataList,
        List<FoodResponse> foodDataList,
        List<String> images,
        boolean isWriter
) {
    public static PostDetailResponse toResponse(String title, String content, List<HealthResponse> healthDataList,
                                                String author, int views,
                                                LocalDateTime date, List<FoodResponse> foodDataList,
                                                List<String> images, boolean isWriter) {
        return new PostDetailResponse(title, content, author, views, date, healthDataList, foodDataList, images, isWriter);
    }
}
