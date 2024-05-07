package org.api.service.post;

import static org.api.exception.ErrorCodes.POST_NOT_FOUND;
import static org.api.exception.ErrorCodes.POST_UPLOAD_FAILED;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.api.entity.FoodEntity;
import org.api.entity.HealthEntity;
import org.api.entity.ImageEntity;
import org.api.entity.PostEntity;
import org.api.entity.PostFoodEntity;
import org.api.entity.PostHealthEntity;
import org.api.entity.PostImageUrlEntity;
import org.api.entity.UserEntity;
import org.api.exception.CustomException;
import org.api.repository.ImageRepository;
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
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final PostImageUrlRepository postImageUrlRepository;
    private final PostFoodService postFoodService;
    private final PostHealthService postHealthService;
    private final FileUploadService fileUploadService;
    private final SaveS3UrlService s3UrlService;
    private final VisitRedisService visitRedisService;
    private final ImageRepository imageRepository;



    @Transactional
    public void saveUploadDetail(PostRequest postRequest, UserEntity user) {
        try {
            PostEntity postEntity = PostEntity.of(user, postRequest);
            postRepository.save(postEntity);

            postFoodService.savePostFood(postEntity, postRequest);
            postHealthService.savePostHealth(postEntity, postRequest);
            List<String> fileUrls = fileUploadService.uploadFiles(postRequest.images()); //s3업로드 방법
            s3UrlService.savePostImageUrl(postEntity, fileUrls);

//            s3UrlService.uploadImagesToDatabase(postRequest.images(), postEntity); // RDB 업로드 방법.

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(POST_UPLOAD_FAILED);
        }
    }

    @Transactional(readOnly = true)
    public PostEntity getPostEntityFindById(Long postId) {
        return postRepository.findByPostId(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_FOUND));
    }

    @Transactional
    public PostDetailResponse getPostDetail(Long postId, UserEntity visitor) {
        PostEntity postEntity = getPostEntityFindById(postId);

        List<FoodEntity> foodEntities = getPostFoodDetailData(postEntity);
        List<HealthEntity> healthEntities = getPostHealthDetailData(postEntity);
//        List<String> imageUrls = getImageUrlsFromBlobs(postId); //DB에 저장된 데이터 get

        List<String> imageUrls = postImageUrlRepository.findByPostPostId(postId).stream()
                .map(PostImageUrlEntity::getImageUrl)
                .collect(Collectors.toList());

        return makePostDetailResponse(postEntity, foodEntities, healthEntities, imageUrls, visitor);
    }

    private List<String> getImageUrlsFromBlobs(long postId)  {
        List<ImageEntity> images = imageRepository.findByPostPostId(postId);
        List<String> imageUrls = new ArrayList<>();  // 리스트 초기화

        for (ImageEntity image : images) {
            byte[] blobBytes = image.getImageData();
            String encodedImage = Base64.getEncoder().encodeToString(blobBytes);
            String imageUrl = "data:image/jpeg;base64," + encodedImage;
            imageUrls.add(imageUrl);
        }
        return imageUrls;
    }

    private List<FoodEntity> getPostFoodDetailData(PostEntity postEntity) {
        return postEntity.getPostFoodEntities().stream()
                .map(PostFoodEntity::getFood)
                .collect(Collectors.toList());
    }

    private List<HealthEntity> getPostHealthDetailData(PostEntity postEntity) {
        return postEntity.getPostHealthEntities().stream()
                .map(PostHealthEntity::getHealth)
                .collect(Collectors.toList());
    }

    private PostDetailResponse makePostDetailResponse(PostEntity postEntity, List<FoodEntity> foodEntities,
                                                      List<HealthEntity> healthEntities, List<String> imageUrls,
                                                      UserEntity visitor) {
        boolean isWriter = false;

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

        if(postEntity.getUser().getUserId().equals(visitor.getUserId())) {
            isWriter = true;
        }

        visitRedisService.addVisitedRedis(visitor, postEntity);

        return PostDetailResponse.toResponse(postEntity.getTitle(), postEntity.getContent(),
                healthResponseList, userName, postEntity.getViews(), postDateTime, foodResponseList, imageUrls, isWriter);
    }


}
