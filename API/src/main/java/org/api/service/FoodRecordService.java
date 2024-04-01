package org.api.service;

import static org.api.exception.ErrorCodes.DELETE_FOOD_DATA_FAILED;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.api.entity.LoginEntity;
import org.api.entity.FoodEntity;
import org.api.entity.UserEntity;
import org.api.exception.CustomException;
import org.api.repository.FoodRepository;
import org.core.request.FoodRequest;
import org.core.response.FoodResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FoodRecordService {
    private final FoodRepository foodRepository;
    private final IngredientService ingredientService;

    @Transactional
    public FoodEntity saveFoodData(FoodRequest foodRequest, UserEntity user) {
        FoodEntity foodEntity = FoodEntity.of(user, foodRequest);
        foodRepository.save(foodEntity);

        return foodEntity;
    }

    @Transactional(readOnly = true)
    public Optional<FoodResponse> getFoodDataById(Long id, UserEntity userEntity) {
        return foodRepository.getFoodDataByFoodRecordIdAndUserUserId(id, userEntity.getUserId())
                .map(this::toFoodResponse);
    }

    @Transactional(readOnly = true)
    public Optional<Object> getFoodDataByDate(LocalDate date, UserEntity userEntity) {
        Optional<List<FoodEntity>> getFoodEntity = foodRepository.getFoodDataByFoodDateAndUserUserId(date,
               userEntity.getUserId());
        if (getFoodEntity.isEmpty()) {
            return Optional.empty();
        } else {
            List<FoodResponse> foodResponseList = getFoodEntity.get().stream()
                    .map(this::toFoodResponse)
                    .collect(Collectors.toList());
            return Optional.of(foodResponseList);
        }
    }

    @Transactional
    public void putFoodData(Long id, FoodRequest foodRequest, UserEntity userEntity) {
        foodRepository.getFoodDataByFoodRecordIdAndUserUserId(id, userEntity.getUserId())
                .ifPresent(orgFoodEntity -> {
                    orgFoodEntity.foodEntityUpdate(foodRequest);
                    ingredientService.putIngredientData(orgFoodEntity, foodRequest);
                });
    }

    @Transactional
    public void deleteFoodData(Long id) {
        try {
            foodRepository.deleteByFoodRecordId(id);
        } catch (Exception e) {
            throw new CustomException(DELETE_FOOD_DATA_FAILED);
        }
    }

    private FoodResponse toFoodResponse(FoodEntity foodEntity) {
        return FoodResponse.toResponse(
                foodEntity.getFoodRecordId(), foodEntity.getFoodDate(), foodEntity.getMealType(),
                foodEntity.getMealTime(), foodEntity.getFoodName(), foodEntity.getIngredientsDtoList(),
                foodEntity.getFoodNotes());
    }
}
