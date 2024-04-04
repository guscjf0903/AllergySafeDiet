package org.api.service;

import lombok.RequiredArgsConstructor;
import org.api.entity.PostEntity;
import org.api.entity.PostFoodEntity;
import org.api.entity.UserEntity;
import org.api.repository.FoodRepository;
import org.api.repository.PostFoodRepository;
import org.api.repository.PostHealthRepository;
import org.api.repository.PostRepository;
import org.core.request.PostRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final FoodRepository foodRepository;
    private final PostHealthRepository postHealthRepository;
    private final PostFoodRepository postFoodRepository;
    @Transactional
    public void saveUploadDetail(PostRequest postRequest, UserEntity user) {
        PostEntity postEntity = PostEntity.of(user, postRequest);
        postRepository.save(postEntity);

        savePostFood(postEntity, postRequest);
    }

    public void savePostFood(PostEntity postEntity, PostRequest postRequest) {
        for(Long foodId : postRequest.foodIds()) {

        }
        foodRepository.findById();

        PostFoodEntity postFoodEntity = new PostFoodEntity(postEntity)
    }
}
