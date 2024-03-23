package org.api.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.api.entity.LoginEntity;
import org.api.entity.FoodEntity;
import org.api.repository.FoodRepository;
import org.core.dto.FoodRequest;
import org.core.response.FoodResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FoodRecordService {
    private final LoginService loginService;
    private final FoodRepository foodRepository;
    private final IngredientService ingredientService;

    @Transactional
    public FoodEntity saveFoodData(FoodRequest foodRequest, String authorizationHeader) {
        LoginEntity loginEntity = loginService.validateLoginId(authorizationHeader);
        FoodEntity foodEntity = FoodEntity.of(loginEntity.getUser(), foodRequest);

        foodRepository.save(foodEntity);

        return foodEntity;
    }

    @Transactional(readOnly = true)
    public Optional<FoodResponse> getFoodDataById(Long id, String authorizationHeader) {
        return foodRepository.getFoodDataByFoodRecordIdAndUserUserId(id, getUserIdFromHeader(authorizationHeader))
                .map(this::toFoodResponse);
    }

    @Transactional(readOnly = true)
    public Optional<Object> getFoodDataByDate(LocalDate date, String authorizationHeader) {
        Optional<List<FoodEntity>> getFoodEntity = foodRepository.getFoodDataByFoodDateAndUserUserId(date,
                getUserIdFromHeader(authorizationHeader));
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
    public void putFoodData(Long id, FoodRequest foodRequest, String authorizationHeader) {
        foodRepository.getFoodDataByFoodRecordIdAndUserUserId(id, getUserIdFromHeader(authorizationHeader))
                .ifPresent(orgFoodEntity -> {
                    orgFoodEntity.foodEntityUpdate(foodRequest);
                    ingredientService.putIngredientData(orgFoodEntity, foodRequest);
                });
    }

    private Long getUserIdFromHeader(String authorizationHeader) {
        return loginService.validateLoginId(authorizationHeader).getUser().getUserId();
    }

    private FoodResponse toFoodResponse(FoodEntity foodEntity) {
        return FoodResponse.toResponse(
                foodEntity.getFoodRecordId(), foodEntity.getFoodDate(), foodEntity.getMealType(),
                foodEntity.getMealTime(), foodEntity.getFoodName(), foodEntity.getIngredientsDtoList(),
                foodEntity.getFoodNotes());
    }
}
