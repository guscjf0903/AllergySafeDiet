package org.api.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.api.entity.LoginEntity;
import org.api.entity.FoodEntity;
import org.api.repository.FoodRepository;
import org.core.dto.MenuDto;
import org.core.response.FoodResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FoodRecordService {
    private final LoginService loginService;
    private final FoodRepository foodRepository;

    @Transactional
    public FoodEntity saveMenuData(MenuDto menuDto, String authorizationHeader) {
        LoginEntity loginEntity = loginService.validateLoginId(authorizationHeader);
        FoodEntity foodEntity = FoodEntity.of(loginEntity.getUser(), menuDto);

        foodRepository.save(foodEntity);

        return foodEntity;
    }

    @Transactional(readOnly = true)
    public Optional<Object> getMenuDataByDate(LocalDate date, String authorizationHeader) {
        LoginEntity loginEntity = loginService.validateLoginId(authorizationHeader);
        Optional<List<FoodEntity>> getFoodEntity = foodRepository.getFoodDataByFoodDateAndUserUserId(date,
                loginEntity.getUser().getUserId());

        if (getFoodEntity.isEmpty()) {
            return Optional.empty();
        } else {
            List<FoodEntity> foodEntityList = getFoodEntity.get();
            List<FoodResponse> foodResponseList = new ArrayList<>();
            for(FoodEntity foodEntity : foodEntityList) {
                FoodResponse foodResponse = FoodResponse.toResponse(
                        foodEntity.getFoodDate(), foodEntity.getMealType(),
                        foodEntity.getMealTime(), foodEntity.getFoodName(),foodEntity.getIngredientsDtoList() ,foodEntity.getFoodNotes());

                foodResponseList.add(foodResponse);
            }

            return Optional.of(foodResponseList);
        }
    }
}
