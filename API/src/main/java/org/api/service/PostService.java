package org.api.service;

import static org.api.exception.ErrorCodes.POST_UPLOAD_FAILED;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.api.entity.FoodEntity;
import org.api.entity.HealthEntity;
import org.api.entity.PostEntity;
import org.api.entity.PostFoodEntity;
import org.api.entity.PostHealthEntity;
import org.api.entity.UserEntity;
import org.api.exception.CustomException;
import org.api.repository.PostFoodRepository;
import org.api.repository.PostHealthRepository;
import org.api.repository.PostRepository;
import org.core.request.PostRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostHealthRepository postHealthRepository;
    private final PostFoodRepository postFoodRepository;
    private final FoodRecordService foodRecordService;
    private final HealthRecordService healthRecordService;
    @Transactional
    public void saveUploadDetail(PostRequest postRequest, UserEntity user) {
        try {
            PostEntity postEntity = PostEntity.of(user, postRequest);
            postRepository.save(postEntity);

            savePostFood(postEntity, postRequest);
            savePostHealth(postEntity, postRequest);
        } catch (Exception e) {
            throw new CustomException(POST_UPLOAD_FAILED);
        }
    }

    private void savePostFood(PostEntity postEntity, PostRequest postRequest) {
        List<FoodEntity> foodEntities = foodRecordService.getFoodDataByIds(postRequest.foodIds());
        if(foodEntities.isEmpty()) {
            return;
        }
        for(FoodEntity foodEntity : foodEntities) {
            PostFoodEntity postFoodEntity = new PostFoodEntity(postEntity, foodEntity);
            postFoodRepository.save(postFoodEntity);
        }
    }

    private void savePostHealth(PostEntity postEntity, PostRequest postRequest) {
        List<HealthEntity> healthEntities = healthRecordService.getHealthDataByIds(postRequest.healthId());
        if(healthEntities.isEmpty()) {
            return;
        }
        for(HealthEntity healthEntity : healthEntities) {
            PostHealthEntity postHealthEntity = new PostHealthEntity(postEntity, healthEntity);
            postHealthRepository.save(postHealthEntity);
        }
    }
}
