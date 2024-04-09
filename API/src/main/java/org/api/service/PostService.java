package org.api.service;

import static org.api.exception.ErrorCodes.POST_NOT_FOUND;
import static org.api.exception.ErrorCodes.POST_UPLOAD_FAILED;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.api.entity.FoodEntity;
import org.api.entity.HealthEntity;
import org.api.entity.PostEntity;
import org.api.entity.PostFoodEntity;
import org.api.entity.PostHealthEntity;
import org.api.entity.PostImageUrlEntity;
import org.api.entity.UserEntity;
import org.api.exception.CustomException;
import org.api.repository.PostFoodRepository;
import org.api.repository.PostHealthRepository;
import org.api.repository.PostImageUrlRepository;
import org.api.repository.PostRepository;
import org.core.request.PostRequest;
import org.core.response.FoodResponse;
import org.core.response.HealthResponse;
import org.core.response.PostDetailResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostHealthRepository postHealthRepository;
    private final PostFoodRepository postFoodRepository;
    private final PostImageUrlRepository postImageUrlRepository;
    private final FoodRecordService foodRecordService;
    private final HealthRecordService healthRecordService;


    @Transactional
    public void saveUploadDetail(PostRequest postRequest, UserEntity user, List<String> fileUrls) {
        try {
            PostEntity postEntity = PostEntity.of(user, postRequest);
            postRepository.save(postEntity);

            savePostFood(postEntity, postRequest);
            savePostHealth(postEntity, postRequest);
            savePostImageUrl(postEntity, fileUrls);
        } catch (Exception e) {
            throw new CustomException(POST_UPLOAD_FAILED);
        }
    }

    private void savePostFood(PostEntity postEntity, PostRequest postRequest) {
        List<FoodEntity> foodEntities = foodRecordService.getFoodDataByIds(postRequest.foodIds());
        if (foodEntities.isEmpty()) {
            return;
        }
        for (FoodEntity foodEntity : foodEntities) {
            PostFoodEntity postFoodEntity = new PostFoodEntity(postEntity, foodEntity);
            postFoodRepository.save(postFoodEntity);
        }
    }

    private void savePostHealth(PostEntity postEntity, PostRequest postRequest) {
        List<HealthEntity> healthEntities = healthRecordService.getHealthDataByIds(postRequest.healthIds());
        if (healthEntities.isEmpty()) {
            return;
        }
        for (HealthEntity healthEntity : healthEntities) {
            PostHealthEntity postHealthEntity = new PostHealthEntity(postEntity, healthEntity);
            postHealthRepository.save(postHealthEntity);
        }
    }

    private void savePostImageUrl(PostEntity postEntity, List<String> fileUrls) {
        for (String fileUrl : fileUrls) {
            PostImageUrlEntity postImageUrlEntity = new PostImageUrlEntity(postEntity, fileUrl);
            postImageUrlRepository.save(postImageUrlEntity);
        }
    }

    @Transactional(readOnly = true)
    public PostDetailResponse getPostDetail(Long postId) {
        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_FOUND));

        List<FoodEntity> foodEntities = getPostFoodDetailData(postId);
        List<HealthEntity> healthEntities = getPostHealthDetailData(postId);
        List<String> imageUrls = postImageUrlRepository.findByPostPostId(postId).stream()
                .map(PostImageUrlEntity::getImageUrl)
                .toList();

        return makePostDetailResponse(postEntity, foodEntities, healthEntities, imageUrls);
    }

    private List<FoodEntity> getPostFoodDetailData(Long postId) {
        return postFoodRepository.findByPostPostId(postId).stream()
                .map(PostFoodEntity::getFood)
                .collect(Collectors.toList());
    }

    private List<HealthEntity> getPostHealthDetailData(Long postId) {
        return postHealthRepository.findByPostPostId(postId).stream()
                .map(PostHealthEntity::getHealth)
                .collect(Collectors.toList());
    }

    private PostDetailResponse makePostDetailResponse(PostEntity postEntity, List<FoodEntity> foodEntities,
                                                      List<HealthEntity> healthEntities, List<String> imageUrls) {
        List<FoodResponse> foodResponseList = foodEntities.stream()
                .map(foodEntity -> FoodResponse.toResponse(
                        foodEntity.getFoodRecordId(), foodEntity.getFoodDate(), foodEntity.getMealType(),
                        foodEntity.getMealTime(), foodEntity.getFoodName(), foodEntity.getIngredientsDtoList(),
                        foodEntity.getFoodNotes()))
                .collect(Collectors.toList());

        List<HealthResponse> healthResponseList = healthEntities.stream()
                .map(healthEntity -> HealthResponse.toResponse(
                        healthEntity.getHealthRecordId(), healthEntity.getHealthDate(), healthEntity.getAllergiesStatus(),
                        healthEntity.getConditionStatus(), healthEntity.getWeight(), healthEntity.getSleepTime(),
                        healthEntity.getHealthNotes(), healthEntity.getPillsDtoList()))
                .collect(Collectors.toList());

        return PostDetailResponse.toResponse(postEntity.getTitle(), postEntity.getContent(),
                healthResponseList, foodResponseList, imageUrls);
    }

}
