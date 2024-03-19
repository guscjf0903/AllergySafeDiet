package org.api.service;

import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.api.entity.LoginEntity;
import org.api.entity.FoodEntity;
import org.api.repository.FoodRepository;
import org.core.dto.MenuDto;
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
    public Optional<?> getMenuDataByDate(LocalDate date, String authorizationHeader) {
        return Optional.empty();
    }

}
