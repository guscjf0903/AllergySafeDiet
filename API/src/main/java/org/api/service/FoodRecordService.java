package org.api.service;

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
    public FoodEntity saveMenuData(MenuDto menuDto) {
        System.out.println(menuDto.getLoginToken());
        LoginEntity loginEntity = loginService.validateLoginId(menuDto.getLoginToken());
        FoodEntity foodEntity = FoodEntity.of(loginEntity.getUser(), menuDto);

        foodRepository.save(foodEntity);

        return foodEntity;
    }

}
