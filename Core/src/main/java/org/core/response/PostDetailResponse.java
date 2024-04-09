package org.core.response;

import java.util.List;

public record PostDetailResponse(
        String title,
        String content,
        List<HealthResponse> healthDataList,
        List<FoodResponse> foodDataList,
        List<String> images
) {
    public static PostDetailResponse toResponse(String title, String content, List<HealthResponse> healthDataList,
                                                List<FoodResponse> foodDataList, List<String> images) {
        return new PostDetailResponse(title, content, healthDataList, foodDataList, images);
    }
}
