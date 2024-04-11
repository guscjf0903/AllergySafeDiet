package org.api.service.post;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.api.entity.FoodEntity;
import org.api.entity.PostEntity;
import org.api.entity.PostFoodEntity;
import org.api.repository.PostFoodRepository;
import org.api.service.FoodRecordService;
import org.core.request.PostRequest;
import org.core.response.FoodResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Service
public class PostFoodService {
    private final FoodRecordService foodRecordService;
    private final PostFoodRepository postFoodRepository;
    public void savePostFood(PostEntity postEntity, PostRequest postRequest) {
        List<FoodEntity> foodEntities = foodRecordService.getFoodDataByIds(postRequest.foodIds());
        if (foodEntities.isEmpty()) {
            return;
        }
        for (FoodEntity foodEntity : foodEntities) {
            PostFoodEntity postFoodEntity = new PostFoodEntity(postEntity, foodEntity);
            postFoodRepository.save(postFoodEntity);
        }
    }
}
