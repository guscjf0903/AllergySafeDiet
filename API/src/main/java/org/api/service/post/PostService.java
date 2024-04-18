package org.api.service.post;

import static org.api.exception.ErrorCodes.POST_NOT_FOUND;
import static org.api.exception.ErrorCodes.POST_UPLOAD_FAILED;

import java.time.LocalDateTime;
import java.util.List;
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
import org.api.service.FileUploadService;
import org.api.service.SaveS3UrlService;
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
    private final PostFoodService postFoodService;
    private final PostHealthService postHealthService;
    private final FileUploadService fileUploadService;
    private final SaveS3UrlService s3UrlService;
    private final VisitRedisService visitRedisService;


    @Transactional
    public void saveUploadDetail(PostRequest postRequest, UserEntity user) {
        try {
            PostEntity postEntity = PostEntity.of(user, postRequest);
            postRepository.save(postEntity);

            postFoodService.savePostFood(postEntity, postRequest);
            postHealthService.savePostHealth(postEntity, postRequest);
            List<String> fileUrls = fileUploadService.uploadFiles(postRequest.images());

            s3UrlService.savePostImageUrl(postEntity, fileUrls);
        } catch (Exception e) {
            throw new CustomException(POST_UPLOAD_FAILED);
        }
    }

    @Transactional(readOnly = true)
    public PostEntity getPostEntityFindById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_FOUND));
    }

    @Transactional
    public PostDetailResponse getPostDetail(Long postId, UserEntity visitor) {
        PostEntity postEntity = getPostEntityFindById(postId);

        List<FoodEntity> foodEntities = getPostFoodDetailData(postId);
        List<HealthEntity> healthEntities = getPostHealthDetailData(postId);
        List<String> imageUrls = postImageUrlRepository.findByPostPostId(postId).stream()
                .map(PostImageUrlEntity::getImageUrl)
                .collect(Collectors.toList());

        return makePostDetailResponse(postEntity, foodEntities, healthEntities, imageUrls, visitor);
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
                                                      List<HealthEntity> healthEntities, List<String> imageUrls,
                                                      UserEntity visitor) {
        List<FoodResponse> foodResponseList = foodEntities.stream()
                .map(foodEntity -> FoodResponse.toResponse(
                        foodEntity.getFoodRecordId(), foodEntity.getFoodDate(), foodEntity.getMealType(),
                        foodEntity.getMealTime(), foodEntity.getFoodName(), foodEntity.getIngredientsDtoList(),
                        foodEntity.getFoodNotes()))
                .collect(Collectors.toList());

        List<HealthResponse> healthResponseList = healthEntities.stream()
                .map(healthEntity -> HealthResponse.toResponse(
                        healthEntity.getHealthRecordId(), healthEntity.getHealthDate(),
                        healthEntity.getAllergiesStatus(),
                        healthEntity.getConditionStatus(), healthEntity.getWeight(), healthEntity.getSleepTime(),
                        healthEntity.getHealthNotes(), healthEntity.getPillsDtoList()))
                .collect(Collectors.toList());

        String userName = postEntity.getUser().getUsername();
        LocalDateTime postDateTime = postEntity.getCreatedAtDate();

        visitRedisService.addVisitedRedis(visitor, postEntity);

        return PostDetailResponse.toResponse(postEntity.getTitle(), postEntity.getContent(),
                healthResponseList, userName, postEntity.getViews(), postDateTime, foodResponseList, imageUrls);
    }


}
